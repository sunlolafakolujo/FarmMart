package com.logicgate.buyer.repository;


import com.logicgate.buyer.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer,Long>, BuyerRepositoryCustom {
}
