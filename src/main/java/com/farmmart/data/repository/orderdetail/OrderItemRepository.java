package com.farmmart.data.repository.orderdetail;

import com.farmmart.data.model.orderitem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long>,OrderItemRepositoryCustom {

}
