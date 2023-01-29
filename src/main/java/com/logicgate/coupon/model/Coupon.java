package com.logicgate.coupon.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.baseaudit.BaseAudit;
import com.logicgate.product.model.Product;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Coupon extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String couponCode;
    private Integer couponDiscount;
    private String couponDescription;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "coupon")
    private Product product;
}
