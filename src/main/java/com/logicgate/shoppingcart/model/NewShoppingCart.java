package com.logicgate.shoppingcart.model;


import com.logicgate.product.model.Product;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewShoppingCart {
    private String shoppingCartCode;
    private Integer orderQuantity;
    private Product product;
    private BigDecimal cartTotal;
}
