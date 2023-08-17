package com.logicgate.shoppingcart.model;


import com.logicgate.baseaudit.BaseObject;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.product.model.Product;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ShoppingCart extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shoppingCartCode;
    private Integer orderQuantity;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Product product;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Buyer buyer;
    private BigDecimal cartTotal;
}
