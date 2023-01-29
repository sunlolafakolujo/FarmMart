package com.logicgate.wishlist.model;

import com.logicgate.buyer.model.Buyer;
import com.logicgate.product.model.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewWishList {
    private Product product;
    private Buyer buyer;
}
