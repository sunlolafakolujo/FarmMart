package com.logicgate.jobdesignation.repository;


import com.logicgate.jobdesignation.model.JobDesignation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDesignationRepository extends JpaRepository<JobDesignation,Long>,JobDesignationRepositoryCustom {
}
