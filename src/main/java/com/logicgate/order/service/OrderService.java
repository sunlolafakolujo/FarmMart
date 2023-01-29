package com.logicgate.order.service;



import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.order.exception.OrderNotFoundException;
import com.logicgate.order.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order) throws AppUserNotFoundException;
    Order fetchOrderById(Long id) throws OrderNotFoundException;
    Order fetchOrderByCode(String orderCode) throws OrderNotFoundException;
    List<Order> fetchBuyerOrders(Integer pageNumber) throws AppUserNotFoundException;
    List<Order> fetchAllOrders(Integer pageNumber);
    void cancelOrder() throws AppUserNotFoundException;
}
