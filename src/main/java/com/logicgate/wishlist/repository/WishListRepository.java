package com.logicgate.wishlist.repository;

import com.logicgate.wishlist.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList,Long>,WishListRepositoryCustom {
}
