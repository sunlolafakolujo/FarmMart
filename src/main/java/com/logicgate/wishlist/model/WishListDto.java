package com.logicgate.wishlist.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WishListDto {
    private Long id;
    private String productType;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String buyerName;
}
