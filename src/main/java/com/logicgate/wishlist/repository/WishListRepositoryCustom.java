package com.logicgate.wishlist.repository;

import com.logicgate.buyer.model.Buyer;
import com.logicgate.wishlist.model.WishList;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListRepositoryCustom {
    @Query("FROM WishList w WHERE w.buyer=?1")
    List<WishList> findWishListByBuyer(Buyer buyer);
}
