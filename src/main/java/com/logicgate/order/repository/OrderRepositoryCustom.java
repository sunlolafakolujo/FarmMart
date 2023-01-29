package com.logicgate.order.repository;


import com.logicgate.buyer.model.Buyer;
import com.logicgate.order.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {
    @Query("FROM Order o WHERE o.orderCode=?1")
    Optional<Order> findOrderByCode(String orderCode);

    @Query("FROM Order o WHERE o.buyer=?1")
    List<Order> findOrderByBuyer(Buyer buyer);

    @Query("FROM Order o WHERE o.buyer=?1")
    Order findOrderByBuyerEmailOrUsernameOrMobile(Buyer buyer);

}
