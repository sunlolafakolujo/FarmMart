package com.logicgate.jobdesignation.controller;


import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.jobdesignation.model.JobDesignationDto;
import com.logicgate.jobdesignation.model.NewJobDesignation;
import com.logicgate.jobdesignation.model.UpdateJobDesignation;
import com.logicgate.jobdesignation.service.JobDesignationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class JobDesignationController {
    private final JobDesignationService jobDesignationService;
    private final ModelMapper modelMapper;

    @PostMapping("/addJobDesignation")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewJobDesignation> addJobDesignation(@RequestBody NewJobDesignation newJobDesignation)
                                                                            throws JobDesignationNotFoundException {
        JobDesignation jobDesignation=modelMapper.map(newJobDesignation, JobDesignation.class);
        JobDesignation post=jobDesignationService.addJobDesignation(jobDesignation);
        NewJobDesignation posted=modelMapper.map(post,NewJobDesignation.class);
        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @GetMapping("/findJobDesignationById")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDesignationDto> getJobDesignationById(@RequestParam("id") Long id) throws JobDesignationNotFoundException {
        JobDesignation jobDesignation=jobDesignationService.fetchById(id);
        return new ResponseEntity<>(convertJobDesignationToDto(jobDesignation),HttpStatus.OK);
    }

    @GetMapping("/findJobDesignationByCodeOrTitle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDesignationDto> getJobDesignationByCodeOrTitle(@RequestParam("searchKey") String searchKey)
                                                                            throws JobDesignationNotFoundException {
        JobDesignation jobDesignation= jobDesignationService.fetchByJobDesignationCodeOrJobTitle(searchKey);
        return new ResponseEntity<>(convertJobDesignationToDto(jobDesignation),HttpStatus.OK);
    }

    @GetMapping("/findAllJobDesignations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JobDesignationDto>> getAllJobDesignation(@RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(jobDesignationService.fetchAllJobDesignation(pageNumber)
                .stream()
                .map(this::convertJobDesignationToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/updateJobDesignation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateJobDesignation> editJobDesignation(@RequestBody UpdateJobDesignation updateJobDesignation,
                                                                   @RequestParam("id") Long id)
                                                                   throws JobDesignationNotFoundException {
        JobDesignation jobDesignation=modelMapper.map(updateJobDesignation,JobDesignation.class);
        JobDesignation post=jobDesignationService.updateJobDesignation(jobDesignation,id);
        UpdateJobDesignation posted=modelMapper.map(post,UpdateJobDesignation.class);
        return new ResponseEntity<>(posted,HttpStatus.OK);
    }

    @DeleteMapping("/deleteJobDesignation")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteJobDesignation(@RequestParam("id") Long id) throws JobDesignationNotFoundException {
        jobDesignationService.deleteJobDesignation(id);
        return ResponseEntity.ok().body("Job designation ID "+id+" is deleted");
    }

    @DeleteMapping("/deleteAllDesignations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllJobDesignations(){
        jobDesignationService.deleteAllJobDesignation();
        return ResponseEntity.ok().body("All Job designations delete");
    }

    JobDesignationDto convertJobDesignationToDto(JobDesignation jobDesignation){
        JobDesignationDto jobDesignationDto=new JobDesignationDto();
        jobDesignationDto.setId(jobDesignation.getId());
        jobDesignationDto.setJobDesignationCode(jobDesignation.getJobDesignationCode());
        jobDesignationDto.setJobTitle(jobDesignation.getJobTitle());
        jobDesignationDto.setJobDescription(jobDesignation.getJobDescription());
        return jobDesignationDto;
    }
}
