package com.farmmart.controller.ordercontroller;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.order.CustomerOrderDto;
import com.farmmart.data.model.order.NewCustomerOrder;
import com.farmmart.data.model.orderitem.OrderItemDto;
import com.farmmart.service.appuser.AppUserService;
import com.farmmart.service.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final AppUserService appUserService;

    private final ModelMapper modelMapper;

    @PostMapping("/placeOrder/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CUSTOMER')")
    public ResponseEntity<NewCustomerOrder> placeOrder(@RequestBody NewCustomerOrder newCustomerOrder,
                                                       @PathVariable(value="username") String username) {

        CustomerOrder customerOrder=modelMapper.map(newCustomerOrder, CustomerOrder.class);
        CustomerOrder post=orderService.placeOrder(customerOrder, username);

        NewCustomerOrder posted=modelMapper.map(post, NewCustomerOrder.class);

        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @GetMapping("/findOrderByCustomer/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CUSTOMER')")
    public ResponseEntity<List<CustomerOrderDto>> getAllCustomerorder(@PathVariable(value = "username") String username)
                                                                      throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserByUsername(username);

        return ResponseEntity.ok().body(orderService.findOrderByUser(appUser, username)
                .stream()
                .map(this::convertCustomerOrderToDto)
                .collect(Collectors.toList()));
    }




    private CustomerOrderDto convertCustomerOrderToDto(CustomerOrder customerOrder){
        CustomerOrderDto customerOrderDto=new CustomerOrderDto();

        customerOrderDto.setOrderDate(customerOrder.getOrderDate());
        customerOrderDto.setStatus(customerOrder.getStatus());
        customerOrderDto.setCustomerName(customerOrder.getAppUser().getCustomer().getFirstName()+" "+
                customerOrder.getAppUser().getCustomer().getLastName());
        customerOrderDto.setId(customerOrder.getId());
        customerOrderDto.setOrderItems(customerOrder.getOrderItems());
        customerOrderDto.setTotalAmount(customerOrder.getTotalAmount());
        customerOrderDto.setPhone(customerOrder.getAppUser().getPhone());

        return customerOrderDto;
    }

    @Data
    @AllArgsConstructor
    private static class OrderForm{

        private List<OrderItemDto> orderItemDto;

    }
}
