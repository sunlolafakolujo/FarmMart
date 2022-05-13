package com.farmmart.data.repository.orderdetail;

import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.orderitem.OrderItem;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepositoryCustom {
    @Query("From OrderItem o Where o.customerOrder=?1")
    OrderItem findOrderItemsByOrderId(CustomerOrder customerOrder);
}
