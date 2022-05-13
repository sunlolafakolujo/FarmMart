package com.farmmart.data.model.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.orderitem.OrderItem;
import com.farmmart.data.model.staticdata.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCustomerOrder {

    private LocalDateTime orderDate;

    private Status status;

    private AppUser appUser;

    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;

}
