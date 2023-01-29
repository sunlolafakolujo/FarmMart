package com.logicgate.shoppingcart.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateShoppingCart {
    private Long id;
    private Integer orderQuantity;
}
