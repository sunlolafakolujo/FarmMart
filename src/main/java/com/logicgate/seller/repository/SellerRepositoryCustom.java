package com.logicgate.seller.repository;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.seller.model.Seller;
import com.logicgate.staticdata.BusinessEntityType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellerRepositoryCustom {
    @Query("FROM Seller s WHERE s.sellerCode=?1 OR s.sellerName=?2 OR s.companyRepresentative=?3 OR s.taxId=?4 OR s.rcNumber=?5")
    Optional<Seller> findSellerByCodeOrNameORepresentativeOrTaxIdOrRcNumber(String sellerCode, String sellerName,
                                                                            String companyRepresentative, String taxId,
                                                                            String rcNumber);

    @Query("FROM Seller s WHERE s.appUser=?1")
    Seller findSellerByUsernameOrEmailOMobile(AppUser appUser);



    @Query("FROM Seller s WHERE s.businessEntityType=?1")
    List<Seller> findSellerByBusinessEntityType(BusinessEntityType businessEntityType, Pageable pageable);

}
