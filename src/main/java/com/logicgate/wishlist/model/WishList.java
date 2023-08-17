package com.logicgate.wishlist.model;

import com.logicgate.baseaudit.BaseObject;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.product.model.Product;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class WishList extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Product product;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Buyer buyer;
}
