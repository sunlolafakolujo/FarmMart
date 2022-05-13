package com.farmmart.service.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.cart.CartDto;
import com.farmmart.data.model.cart.CartItemDto;
import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.order.OrderNotFoundException;
import com.farmmart.data.model.orderitem.OrderItem;
import com.farmmart.data.model.staticdata.Status;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.order.OrderRepository;
import com.farmmart.data.repository.orderdetail.OrderItemRepository;
import com.farmmart.service.cart.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Override
    public CustomerOrder placeOrder(CustomerOrder customerOrder, String username) {
        log.info("Create customer order");
        AppUser appUser=appUserRepository.findByUserName(username);

        CartDto cartDto= cartService.listCartItems(appUser,username);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        customerOrder.setOrderDate(LocalDate.now());
        customerOrder.setAppUser(appUser);
        customerOrder.setStatus(Status.PAID);
//        customerOrder.setVatAmount(cartDto.getTotal().multiply(BigDecimal.valueOf(0.075)));
        customerOrder.setTotalAmount(cartDto.getTotal());


        for (CartItemDto cartItemDto : cartItemDtoList) {

            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(LocalDate.now());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());

        }

        return orderRepository.save(customerOrder);
    }

    @Override
    public CustomerOrder findOrderById(Long id) {
        log.info("Find order by id");

        CustomerOrder order=orderRepository.findById(id).orElseThrow();

        return order;
    }

    @Override
    public List<CustomerOrder> findOrderByUser(AppUser appUser, String username) {
        log.info("Fetch all user orders");

        appUser=appUserRepository.findByUserName(username);

        List<CustomerOrder> orderByUser=orderRepository.findOrderByAppUser(appUser);

        return orderByUser;
    }

    @Override
    public List<CustomerOrder> findAllOrder() {
        log.info("Fetch all Orders");

        return orderRepository.findAll();
    }

    @Override
    public void deleteOrderById(Long id) throws OrderNotFoundException {
        log.info("Delete order by Id");

        orderRepository.deleteById(id);

        Optional<CustomerOrder> optionalOrder=orderRepository.findById(id);

        if (optionalOrder.isEmpty()){
            log.info("Order is deleted");
        }else throw new OrderNotFoundException("Order id "+id+" is Not Deleted");
    }

    @Override
    public void deleteOrderByUser(AppUser appUser, String username) {
        appUser=appUserRepository.findByUserName(username);

        List<CustomerOrder> userOrders=orderRepository.findOrderByAppUser(appUser);

        orderRepository.deleteAll(userOrders);
    }

    @Override
    public void deleteAllOrder() {
        orderRepository.deleteAll();

    }
}
