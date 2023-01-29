package com.logicgate.jobdesignation.repository;


import com.logicgate.jobdesignation.model.JobDesignation;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JobDesignationRepositoryCustom {
    @Query("FROM JobDesignation j WHERE j.jobDesignationCode=?1 OR j.jobTitle=?2")
    Optional<JobDesignation> findByJobDesignationCodeOrJobTitle(String jobDesignationCode, String jobTitle);
}
