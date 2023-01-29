package com.logicgate.seller.repository;


import com.logicgate.seller.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long>, SellerRepositoryCustom {
}
