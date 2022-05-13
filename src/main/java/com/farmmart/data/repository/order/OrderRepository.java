package com.farmmart.data.repository.order;

import com.farmmart.data.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder,Long>,OrderRepositoryCustom{
}
