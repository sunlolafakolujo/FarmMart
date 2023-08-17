package com.logicgate.order.model;


import com.logicgate.buyer.model.Buyer;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.staticdata.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewOrder {
    private String orderCode;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private OrderStatus orderStatus;
    private Buyer buyer;
    private List<ShoppingCart> shoppingCarts;
}
