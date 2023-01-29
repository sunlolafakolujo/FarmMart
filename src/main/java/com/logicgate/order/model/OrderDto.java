package com.logicgate.order.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.staticdata.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String orderCode;
    private OrderStatus orderStatus;
    private String buyerName;
    private String phone;
    private List<ShoppingCart> shoppingCarts;
}
