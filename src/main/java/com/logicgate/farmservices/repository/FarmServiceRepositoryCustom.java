package com.logicgate.farmservices.repository;

import com.logicgate.farmservices.model.FarmService;
import com.logicgate.seller.model.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FarmServiceRepositoryCustom {
    @Query("FROM FarmService f WHERE f.serviceCode=?1")
    Optional<FarmService> findByServiceCode(String serviceCode);

    @Query("FROM FarmService f WHERE f.serviceName=?1 Or f.serviceDescription=?2")
    List<FarmService> findByServiceNameOrDescription(String serviceName,
                                                     String serviceDescription,
                                                     Pageable pageable);

    @Query("FROM FarmService f WHERE f.seller=?1")
    List<FarmService> findServiceBySeller(Seller seller);

    @Modifying
    @Query("DELETE FROM FarmService f WHERE f.serviceCode=?1 AND f.seller=?2")
    void deleteSellerServiceByCode(String serviceCode, Seller seller);
}
