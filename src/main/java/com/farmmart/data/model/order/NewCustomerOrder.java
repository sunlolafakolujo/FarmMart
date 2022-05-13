package com.farmmart.data.model.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.orderitem.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrder {

    private LocalDateTime orderDate;

    private AppUser appUser;

    private List<OrderItem> orderItems;

}
