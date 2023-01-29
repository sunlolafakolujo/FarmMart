package com.logicgate.order.model;


import com.logicgate.buyer.model.Buyer;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.staticdata.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewOrder {
    private String orderCode;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private OrderStatus orderStatus;
    private Buyer buyer;
    private List<ShoppingCart> shoppingCarts;
}
