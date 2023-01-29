package com.logicgate.product.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.category.model.Category;
import com.logicgate.colour.model.Colour;
import com.logicgate.coupon.model.Coupon;
import com.logicgate.image.model.Picture;
import com.logicgate.seller.model.Seller;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.staticdata.ProductAvailability;
import com.logicgate.staticdata.ProductCondition;
import com.logicgate.staticdata.UnitOfMeasure;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productCode;
    private String productType;
    private String productName;

    @Column(length = 50000)
    private String productDescription;
    private String manufacturerWebSite;

    @Positive
    private BigDecimal price;

    @Transient
    private BigDecimal discountedPrice;

//    @Positive
    private Integer availableStockQuantity;
    private String brand;

    @Column(length = 1000)
    private String specification;
    private String productSKU;

    @Enumerated(EnumType.STRING)
    private ProductCondition productCondition;

    @Enumerated(EnumType.STRING)
    private UnitOfMeasure unitOfMeasure;

    @Enumerated(EnumType.STRING)
    private ProductAvailability productAvailability;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Category> categories;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Coupon coupon;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "product_colours",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "colour_id", referencedColumnName = "id"))
    private List<Colour> colours=new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "product_pictures",
    joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id"))
    private List<Picture> pictures=new ArrayList<>();

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Seller seller;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShoppingCart> shoppingCarts;
}
