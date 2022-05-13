package com.farmmart.data.repository.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.Status;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepositoryCustom {

    @Query("FROM Employee e WHERE e.firstName LIKE %?#{[0].toUpperCase()}%")
    List<Employee> findByFirstName(String firstName);

    @Query("FROM Employee e WHERE e.lastName LIKE %?#{[0].toUpperCase()}%")
    List<Employee> findByLastName(String lastName);

    @Query("FROM Employee e WHERE e.gender=?1")
    List<Employee> findByGender(Gender gender);

    @Query("FROM Employee e WHERE e.employeeStatus=?1")
    List<Employee> findByStatus(Status status);

    @Query("FROM Employee e WHERE e.appUser=?1")
    Employee findByUsername(AppUser appUser);
}
