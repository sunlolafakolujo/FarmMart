package com.logicgate.coupon.service;



import com.logicgate.coupon.exception.CouponNotFoundException;
import com.logicgate.coupon.model.Coupon;
import com.logicgate.product.exception.ProductNotFoundException;

import java.util.List;

public interface CouponService {
    Coupon addCoupon(Coupon coupon) throws CouponNotFoundException;
    void addCouponDiscountToProduct(String productCode, Integer couponDiscount) throws ProductNotFoundException, CouponNotFoundException;
    Coupon fetchCouponById(Long id) throws CouponNotFoundException;
    Coupon fetchCouponByCode(String couponCode) throws CouponNotFoundException;
    List<Coupon> fetchAllCoupons(Integer pageNumber);
    Coupon updateCoupon(Coupon coupon,Long id) throws CouponNotFoundException;
    void deleteCoupon(Long id) throws CouponNotFoundException;
    void deleteAllCoupons();
}
