package com.logicgate.wishlist.service;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.wishlist.exception.WishListNotFoundException;
import com.logicgate.wishlist.model.WishList;

import java.util.List;

public interface WishListService {
    WishList addProductToWishList(WishList wishList) throws AppUserNotFoundException, WishListNotFoundException;
    List<WishList> fetchWishListByBuyer() throws AppUserNotFoundException;
    void deleteWishList(Long id) throws WishListNotFoundException;
    void deleteAllWishList() throws AppUserNotFoundException;
}
