package com.logicgate.order.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.service.AppUserService;
import com.logicgate.buyer.exception.BuyerNotFoundException;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.service.BuyerService;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.event.InvoiceEvent;
import com.logicgate.invoice.model.Invoice;
import com.logicgate.invoice.service.InvoiceService;
import com.logicgate.order.exception.OrderNotFoundException;
import com.logicgate.order.model.NewOrder;
import com.logicgate.order.model.Order;
import com.logicgate.order.model.OrderDto;
import com.logicgate.order.service.OrderService;
import com.logicgate.shoppingcart.exception.ShoppingCartNotFoundException;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.shoppingcart.service.ShoppingCartService;
import com.logicgate.staticdata.OrderStatus;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final BuyerService buyerService;
    private final AppUserService appUserService;
    private final ShoppingCartService shoppingCartService;
    private final InvoiceService invoiceService;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Invoice> placeOrder(@RequestBody NewOrder newOrder, HttpServletRequest request)
                                                        throws UserRoleNotFoundException, BuyerNotFoundException,
                                                        AppUserNotFoundException, ShoppingCartNotFoundException {
        Invoice invoice=new Invoice();
        BigDecimal cartAmount=shoppingCartService.getShoppingCartAmount(newOrder.getShoppingCarts());
        String searchKey= JwtRequestFilter.CURRENT_USER;
        Buyer buyer=buyerService.fetchBuyerByUsernameOrEmailOrMobile(searchKey);
//        Buyer buyer=new Buyer(newOrder.getBuyer().getBuyerCode(),newOrder.getBuyer().getFirstName(), newOrder.getBuyer().getLastName());
        Long isBuyerPresent=buyerService.isBuyerPresent(buyer);
        if (isBuyerPresent!=null){
            buyer.setId(isBuyerPresent);
        }else {
            buyer=buyerService.addBuyer(buyer);
        }
        Order order=new Order(newOrder.getOrderCode(),newOrder.getOrderDate(),newOrder.getDeliveryDate(),buyer
                , OrderStatus.PLACED,newOrder.getShoppingCarts());
        Order purchase=orderService.placeOrder(order);

        invoice.setOrder(purchase);
        invoice.setInvoiceDate(new Date());
        invoice.setTotalBeforeTax(cartAmount);
        invoice.setVatAmount((cartAmount.multiply(new BigDecimal(7.5))
                .divide(new BigDecimal(100)))
                .setScale(2, RoundingMode.UP));
        invoice.setInvoiceTotal(invoice.getVatAmount().add(cartAmount));
        Invoice orderInvoice=invoiceService.addInvoiceToOrder(invoice);
        publisher.publishEvent(new InvoiceEvent(purchase, applicationUrl(request)));
        return new ResponseEntity<>(orderInvoice, CREATED);
    }

    @GetMapping("/findOrder")
    public ResponseEntity<OrderDto> getOrderById(@RequestParam("id")Long id) throws OrderNotFoundException {
        Order order=orderService.fetchOrderById(id);
        return new ResponseEntity<>(convertOrderToDto(order), OK);
    }

    @GetMapping("/findByCode")
    public ResponseEntity<OrderDto> getOrderByCode(@RequestParam("code") String orderCode) throws OrderNotFoundException {
        Order order= orderService.fetchOrderByCode(orderCode);
        return new ResponseEntity<>(convertOrderToDto(order),OK);
    }

    @GetMapping("/findBuyerOrder")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<OrderDto>> getBuyerOrder(@RequestParam("pageNumber")Integer pageNumber) throws AppUserNotFoundException {
        return new ResponseEntity<>(orderService.fetchBuyerOrders(pageNumber)
                .stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestParam("pageNumber")Integer pageNumber){
        return new ResponseEntity<>(orderService.fetchAllOrders(pageNumber)
                .stream()
                .map(this::convertOrderToDto)
                .collect(Collectors.toList()), OK);
    }

    @DeleteMapping("/cancelOrder")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> cancelOrder() throws AppUserNotFoundException {
        orderService.cancelOrder();
        return ResponseEntity.ok().body("Your Order has being cancelled");
    }


    private OrderDto convertOrderToDto(Order order){
        OrderDto orderDto=new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderCode(order.getOrderCode());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setBuyerName(order.getBuyer().getFirstName().concat(" ").concat(order.getBuyer().getLastName()));
        orderDto.setPhone(order.getBuyer().getAppUser().getMobile());
        orderDto.setShoppingCarts(order.getShoppingCarts());
        return orderDto;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
