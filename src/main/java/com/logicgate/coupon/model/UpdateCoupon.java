package com.logicgate.coupon.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateCoupon {
    private Long id;
    private Integer couponDiscount;
    private String couponDescription;
}
