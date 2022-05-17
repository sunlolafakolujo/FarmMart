package com.farmmart.data.model.wishlist;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewWishList {

    private AppUser appUser;

    private Product product;
}
