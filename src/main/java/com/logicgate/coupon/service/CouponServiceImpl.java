package com.logicgate.coupon.service;


import com.logicgate.coupon.exception.CouponNotFoundException;
import com.logicgate.coupon.model.Coupon;
import com.logicgate.coupon.repository.CouponRepository;
import com.logicgate.product.exception.ProductNotFoundException;
import com.logicgate.product.model.Product;
import com.logicgate.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Coupon addCoupon(Coupon coupon) throws CouponNotFoundException {
        coupon.setCouponCode("CPN".concat(String.valueOf(new Random().nextInt(100000000))));
        Optional<Coupon> savedCoupon=couponRepository.findByCouponDiscount(coupon.getCouponDiscount());
        if (savedCoupon.isPresent()){
            throw new CouponNotFoundException("Coupon discount already exist");
        }
        coupon.setCouponDescription(String.valueOf(coupon.getCouponDiscount()).concat("% OFF"));
        return couponRepository.save(coupon);
    }

    @Override
    public void addCouponDiscountToProduct(String productCode, Integer couponDiscount) throws ProductNotFoundException,
                                                                                        CouponNotFoundException {
        Product product=productRepository.findByProductCode(productCode)
                .orElseThrow(()->new ProductNotFoundException("Coupon code "+productCode+" Not Found"));
        Coupon coupon=couponRepository.findByCouponDiscount(couponDiscount)
                .orElseThrow(()->new CouponNotFoundException("Coupon discount "+couponDiscount+" Not Found"));
        product.setCoupon(coupon);
        productRepository.save(product);
    }

    @Override
    public Coupon fetchCouponById(Long id) throws CouponNotFoundException {
        return couponRepository.findById(id).orElseThrow(()->new CouponNotFoundException("Coupon ID "+id+" Not Found"));
    }

    @Override
    public Coupon fetchCouponByCode(String couponCode) throws CouponNotFoundException {
        return couponRepository.findByCouponCode(couponCode).
                orElseThrow(()->new CouponNotFoundException("Coupon code "+couponCode+" Not Found"));
    }

    @Override
    public List<Coupon> fetchAllCoupons(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return couponRepository.findAll(pageable).toList();
    }

    @Override
    public Coupon updateCoupon(Coupon coupon, Long id) throws CouponNotFoundException {
        Coupon savedCoupon=couponRepository.findById(id)
                .orElseThrow(()->new CouponNotFoundException("Coupon ID "+id+" Not Found"));
        if (Objects.nonNull(coupon.getCouponDiscount()) && !"".equals(coupon.getCouponDiscount())){
            savedCoupon.setCouponDiscount(coupon.getCouponDiscount());
        }if (Objects.nonNull(coupon.getCouponDescription()) && !"".equalsIgnoreCase(coupon.getCouponDescription())){
            savedCoupon.setCouponDescription(coupon.getCouponDescription());
        }
        return couponRepository.save(savedCoupon);
    }

    @Override
    public void deleteCoupon(Long id) throws CouponNotFoundException {
        if (couponRepository.existsById(id)){
            couponRepository.deleteById(id);
        }else {
            throw new CouponNotFoundException("Coupon ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllCoupons() {
        couponRepository.deleteAll();
    }
}
