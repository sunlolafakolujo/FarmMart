package com.logicgate.order.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.order.exception.OrderNotFoundException;
import com.logicgate.order.model.Order;
import com.logicgate.order.repository.OrderRepository;
import com.logicgate.shoppingcart.repository.ShoppingCartRepository;
import com.logicgate.staticdata.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Override
    public Order placeOrder(Order order) throws AppUserNotFoundException {
//        ShoppingCart shoppingCart=new ShoppingCart();
//        shoppingCart.setShoppingCartCode("CART".concat(String.valueOf(new Random().nextInt(100000000))));
//        List<ShoppingCart> shoppingCarts=new ArrayList<>();
//        shoppingCarts.add(shoppingCart);
//        order.setShoppingCarts(shoppingCarts);
        order.setOrderCode("ORDER".concat(String.valueOf(new Random().nextInt(100000000))));
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveryDate(order.getOrderDate().plusDays(10));
        order.setOrderStatus(OrderStatus.PLACED);
//        order.setShoppingCarts(order.getShoppingCarts());
        return orderRepository.save(order);
    }

    @Override
    public Order fetchOrderById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Order ID "+id+" Not Found"));
    }

    @Override
    public Order fetchOrderByCode(String orderCode) throws OrderNotFoundException {
        return orderRepository.findOrderByCode(orderCode)
                .orElseThrow(()->new OrderNotFoundException("Order number "+orderCode+" Not Found"));
    }

    @Override
    public List<Order> fetchBuyerOrders(Integer pageNumber) throws AppUserNotFoundException {
        Pageable pageable=PageRequest.of(pageNumber,10);
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        return orderRepository.findOrderByBuyer(buyer);
    }

    @Override
    public List<Order> fetchAllOrders(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return orderRepository.findAll(pageable).toList();
    }

    @Override
    public void cancelOrder() throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        Order buyerOrder=orderRepository.findOrderByBuyerEmailOrUsernameOrMobile(buyer);
        orderRepository.delete(buyerOrder);
    }
}
