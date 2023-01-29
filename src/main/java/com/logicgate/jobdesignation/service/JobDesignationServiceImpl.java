package com.logicgate.jobdesignation.service;


import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.jobdesignation.repository.JobDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class JobDesignationServiceImpl implements JobDesignationService{
    @Autowired
    private JobDesignationRepository jobDesignationRepository;

    @Override
    public JobDesignation addJobDesignation(JobDesignation jobDesignation) throws JobDesignationNotFoundException {
        Optional<JobDesignation> savedJobDesignation=jobDesignationRepository
                .findByJobDesignationCodeOrJobTitle(jobDesignation.getJobDesignationCode(), jobDesignation.getJobTitle());
        if (savedJobDesignation.isPresent()){
            throw new JobDesignationNotFoundException("Job Designation already exist");
        }
        jobDesignation.setJobDesignationCode("JD".concat(String.valueOf(new Random().nextInt(100000))));
        return jobDesignationRepository.save(jobDesignation);
    }

    @Override
    public JobDesignation fetchById(Long id) throws JobDesignationNotFoundException {
        return jobDesignationRepository.findById(id)
                .orElseThrow(()->new JobDesignationNotFoundException("Job Designation Not Found"));
    }

    @Override
    public JobDesignation fetchByJobDesignationCodeOrJobTitle(String searchKey) throws JobDesignationNotFoundException {
        return jobDesignationRepository.findByJobDesignationCodeOrJobTitle(searchKey,searchKey)
                .orElseThrow(()->new JobDesignationNotFoundException("Job Designation Not Found"));
    }

    @Override
    public List<JobDesignation> fetchAllJobDesignation(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return jobDesignationRepository.findAll(pageable).toList();
    }

    @Override
    public JobDesignation updateJobDesignation(JobDesignation jobDesignation, Long id) throws JobDesignationNotFoundException {
        JobDesignation savedJob=jobDesignationRepository.findById(id)
                .orElseThrow(()->new JobDesignationNotFoundException("Job Designation Not Found"));
        if (Objects.nonNull(jobDesignation.getJobTitle()) && !"".equalsIgnoreCase(jobDesignation.getJobTitle())){
            savedJob.setJobTitle(jobDesignation.getJobTitle());
        }if (Objects.nonNull(jobDesignation.getJobDescription()) && !"".equalsIgnoreCase(jobDesignation.getJobDescription())){
            savedJob.setJobDescription(jobDesignation.getJobDescription());
        }
        return jobDesignationRepository.save(savedJob);
    }

    @Override
    public void deleteJobDesignation(Long id) throws JobDesignationNotFoundException {
        if (jobDesignationRepository.existsById(id)){
            jobDesignationRepository.deleteById(id);
        }else {
            throw new JobDesignationNotFoundException("Jod Designation ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllJobDesignation() {
        jobDesignationRepository.deleteAll();
    }
}
