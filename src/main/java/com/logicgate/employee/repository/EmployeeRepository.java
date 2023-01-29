package com.logicgate.employee.repository;


import com.logicgate.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Long>,EmployeeRepositoryCustom {
}
