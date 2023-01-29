package com.logicgate.coupon.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddCouponToProduct {
    private String productCode;
    private Integer couponDiscount;
}
