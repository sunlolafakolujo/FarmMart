package com.farmmart.service.cart;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.cart.Cart;
import com.farmmart.data.model.cart.CartDto;
import com.farmmart.data.model.cart.CartNotFoundException;

import java.util.List;

public interface CartService {

    Cart addToCart(Cart newCart);
    Cart findCartById(Long id);
//    CartDto listCartItems(AppUser appUser);
    CartDto listCartItems(AppUser appUser, String username);
    List<Cart> findAllCarts();
    Cart updateCart(Cart cart, Long id) throws CartNotFoundException;
    void deleteCartById(Long id);
    void deleteCartByAppUser(String username) throws CartNotFoundException;
    void deleteAllCarts();
}
