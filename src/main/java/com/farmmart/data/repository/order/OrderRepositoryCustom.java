package com.farmmart.data.repository.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.order.CustomerOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepositoryCustom {
    @Query("From CustomerOrder c Where c.appUser=?1")
    List<CustomerOrder> findOrderByAppUser(AppUser appUser);
}
