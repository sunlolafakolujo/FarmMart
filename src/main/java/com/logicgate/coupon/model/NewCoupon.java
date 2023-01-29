package com.logicgate.coupon.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewCoupon {
    private String couponCode;
    private Integer couponDiscount;
}
