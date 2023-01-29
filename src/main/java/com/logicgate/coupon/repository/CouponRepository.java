package com.logicgate.coupon.repository;

import com.logicgate.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long>,CouponRepositoryCustom {
}
