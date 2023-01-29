package com.logicgate.shoppingcart.repository;


import com.logicgate.shoppingcart.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long>,ShoppingCartRepositoryCustom {
}
