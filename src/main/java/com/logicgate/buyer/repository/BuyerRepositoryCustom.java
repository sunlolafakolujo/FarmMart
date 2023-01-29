package com.logicgate.buyer.repository;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.staticdata.Gender;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuyerRepositoryCustom {
    @Query("FROM Buyer b WHERE b.buyerCode=?1")
    Optional<Buyer> findBuyerByCode(String buyerCode);

    @Query("FROM Buyer b WHERE b.firstName=?1 OR b.lastName=?2 OR b.otherName=?3")
    List<Buyer> findBuyerByFirstNameOrLastNameOtherName(String firstName, String lastName,
                                                        String otherName, Pageable pageable);

    @Query("FROM Buyer b WHERE b.gender=?1")
    List<Buyer> findBuyerByGender(Gender gender, Pageable pageable);

    @Query("FROM Buyer b WHERE b.age BETWEEN ?1 AND ?2")
    List<Buyer> findByBuyerWithinAgeRange(Integer age1, Integer age2, Pageable pageable);

    @Query("FROM Buyer b WHERE b.age>=?1")
    List<Buyer> findBuyerByAgeLimit(Integer age, Pageable pageable);

    @Query("FROM Buyer b WHERE b.appUser=?1")
    Buyer findBuyerByUsernameOrEmailOrPassword(AppUser appUser);
}
