package com.logicgate.shoppingcart.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShoppingCartDto {
    private Long id;
    private String shoppingCartCode;
    private Integer orderQuantity;
    private BigDecimal productPrice;
    private String productName;
    private String productDescription;
    private BigDecimal cartTotal;
}
