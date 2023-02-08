package com.logicgate.farmservices.repository;

import com.logicgate.farmservices.model.FarmService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmServiceRepository extends JpaRepository<FarmService,Long>, FarmServiceRepositoryCustom {
}
