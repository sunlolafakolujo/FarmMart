package com.logicgate.shoppingcart.repository;



import com.logicgate.buyer.model.Buyer;
import com.logicgate.shoppingcart.model.ShoppingCart;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepositoryCustom {
    @Query("FROM ShoppingCart s WHERE s.shoppingCartCode=?1")
    Optional<ShoppingCart> findByShoppingCartCode(String shoppingCartCode);

    @Query("FROM ShoppingCart s WHERE s.buyer=?1")
    List<ShoppingCart> findShoppingCartByBuyer(Buyer buyer);

}
