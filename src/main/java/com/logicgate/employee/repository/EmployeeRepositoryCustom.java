package com.logicgate.employee.repository;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.employee.model.Employee;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryCustom {
    @Query("FROM Employee e WHERE e.firstName=?1 OR e.lastName=?2 OR e.otherNames=?3 OR e.stateOfOrigin=?4")
    List<Employee> findByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(String key1, String key2,
                                                                        String key3, String key4, Pageable pageable);
    @Query("FROM Employee e WHERE e.appUser=?1")
    Employee findByAppUser(AppUser appUser);

    @Query("FROM Employee e WHERE e.jobDesignation=?1")
    List<Employee> findByJobDesignation(JobDesignation jobDesignation, Pageable pageable);

    @Query("FROM Employee e WHERE e.gender=?1")
    List<Employee> findEmployeeByGender(Gender gender, Pageable pageable);

    @Query("FROM Employee e WHERE e.maritalStatus=?1")
    List<Employee> findEmployeeByMaritalStatus(MaritalStatus maritalStatus, Pageable pageable);

    @Query("FROM Employee e WHERE e.employeeStatus=?1")
    List<Employee> findByEmployeeStatus(EmployeeStatus employeeStatus, Pageable pageable);

    @Query("FROM Employee e WHERE e.employeeCode=?1")
    Optional<Employee> findEmployeeByCode(String employeeCode);
}
