package com.farmmart.data.repository.wishlist;

import com.farmmart.data.model.wishlist.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Long>,WishListRepositoryCustom {
}
