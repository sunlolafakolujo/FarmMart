package com.farmmart.service.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.order.OrderNotFoundException;

import java.util.List;

public interface OrderService {

    CustomerOrder placeOrder(CustomerOrder customerOrder, String username);
    CustomerOrder findOrderById(Long id);
    List<CustomerOrder> findOrderByUser(AppUser appUser, String username);
    List<CustomerOrder> findAllOrder(Integer limit);//page it.
    void deleteOrderById(Long id) throws OrderNotFoundException;
    void deleteOrderByUser(AppUser appUser,String username);
    void deleteAllOrder();


}
