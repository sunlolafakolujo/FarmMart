package com.logicgate.coupon.controller;


import com.logicgate.coupon.exception.CouponNotFoundException;
import com.logicgate.coupon.model.*;
import com.logicgate.coupon.service.CouponService;
import com.logicgate.product.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class CouponController {
    private final CouponService couponService;
    private final ModelMapper modelMapper;

    @PostMapping("/addCoupon")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewCoupon> addCoupon(@RequestBody NewCoupon newCoupon) throws CouponNotFoundException {
        Coupon coupon=modelMapper.map(newCoupon,Coupon.class);
        Coupon post=couponService.addCoupon(coupon);
        NewCoupon posted=modelMapper.map(post,NewCoupon.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @PostMapping("/addCouponToProduct")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> addCouponToProduct(@RequestBody AddCouponToProduct addCouponToProduct) throws ProductNotFoundException,
                                                                                            CouponNotFoundException {
        couponService.addCouponDiscountToProduct(addCouponToProduct.getProductCode(),
                addCouponToProduct.getCouponDiscount());
        return ResponseEntity.ok().body("Coupon "+addCouponToProduct.getCouponDiscount()+
                "has being successfully added to product "+addCouponToProduct.getProductCode());
    }

    @GetMapping("/findCoupon")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<CouponDto> getCouponById(@RequestParam("id") long id) throws CouponNotFoundException {
        Coupon coupon=couponService.fetchCouponById(id);
        return new ResponseEntity<>(convertCouponToDto(coupon),OK);
    }

    @GetMapping("/findCouponByCode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponDto> getCouponByCode(@RequestParam("couponCode") String couponCode) throws CouponNotFoundException {
        Coupon coupon=couponService.fetchCouponByCode(couponCode);
        return new ResponseEntity<>(convertCouponToDto(coupon),OK);
    }

    @GetMapping("/findAllCoupons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CouponDto>> getAllCoupons(@RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(couponService.fetchAllCoupons(pageNumber)
                .stream()
                .map(this::convertCouponToDto)
                .collect(Collectors.toList()), OK);
    }

    @PutMapping("/updateCoupon")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<UpdateCoupon> editCoupon(@RequestBody UpdateCoupon updateCoupon,
                                                   @RequestParam("id") Long id) throws CouponNotFoundException {
        Coupon coupon=modelMapper.map(updateCoupon,Coupon.class);
        Coupon post=couponService.updateCoupon(coupon,id);
        UpdateCoupon posted=modelMapper.map(post,UpdateCoupon.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteCoupon")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCouponById(@RequestParam("id") Long id) throws CouponNotFoundException {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().body("Coupon ID "+id+" has being deleted");
    }

    @DeleteMapping("/deleteAllCoupons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllCoupon() {
        couponService.deleteAllCoupons();
        return ResponseEntity.ok().body("Coupons has being deleted");
    }


    private CouponDto convertCouponToDto(Coupon coupon) {
        CouponDto couponDto=new CouponDto();
        couponDto.setId(coupon.getId());
        couponDto.setCouponCode(coupon.getCouponCode());
        couponDto.setCouponDiscount(coupon.getCouponDiscount());
        couponDto.setCouponDescription(coupon.getCouponDescription());
        return couponDto;
    }
}
