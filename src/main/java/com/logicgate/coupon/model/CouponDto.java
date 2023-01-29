package com.logicgate.coupon.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CouponDto {
    private Long id;
    private String couponCode;
    private Integer couponDiscount;
    private String couponDescription;
}
