package com.logicgate.shoppingcart.service;



import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.shoppingcart.exception.ShoppingCartNotFoundException;
import com.logicgate.shoppingcart.model.ShoppingCart;

import java.math.BigDecimal;
import java.util.List;


public interface ShoppingCartService {
    ShoppingCart addProductToShoppingCart(ShoppingCart shoppingCart) throws AppUserNotFoundException;
    ShoppingCart fetchCartById(Long id) throws ShoppingCartNotFoundException;
    ShoppingCart fetchCartByCode(String shoppingCartCode) throws ShoppingCartNotFoundException;
    List<ShoppingCart> fetchBuyerShoppingCart() throws AppUserNotFoundException;
    List<ShoppingCart> fetchAllShoppingCart(Integer pageNumber);
    ShoppingCart updateShoppingCart(ShoppingCart shoppingCart, Long id) throws ShoppingCartNotFoundException;
    BigDecimal getShoppingCartAmount(List<ShoppingCart> shoppingCarts) throws AppUserNotFoundException, ShoppingCartNotFoundException;
    void deleteCartByBuyer() throws AppUserNotFoundException;
    void deleteShoppingCart(Long id) throws ShoppingCartNotFoundException;
    void deleteAllShoppingCarts();
}
