package com.logicgate.jobdesignation.service;


import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.jobdesignation.model.JobDesignation;

import java.util.List;

public interface JobDesignationService {
    JobDesignation addJobDesignation(JobDesignation jobDesignation) throws JobDesignationNotFoundException;
    JobDesignation fetchById(Long id) throws JobDesignationNotFoundException;
    JobDesignation fetchByJobDesignationCodeOrJobTitle(String searchKey) throws JobDesignationNotFoundException;
    List<JobDesignation> fetchAllJobDesignation(Integer pageNumber);
    JobDesignation updateJobDesignation(JobDesignation jobDesignation, Long id) throws JobDesignationNotFoundException;
    void deleteJobDesignation(Long id) throws JobDesignationNotFoundException;
    void deleteAllJobDesignation();
}
