package com.logicgate.coupon.repository;


import com.logicgate.coupon.model.Coupon;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponRepositoryCustom {
    @Query("FROM Coupon c WHERE c.couponCode=?1")
    Optional<Coupon> findByCouponCode(String couponCode);

    @Query("FROM Coupon c WHERE c.couponDiscount=?1")
    Optional<Coupon> findByCouponDiscount(Integer couponDiscount);
}
