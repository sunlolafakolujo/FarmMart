package com.logicgate.employee.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.employee.exception.EmployeeNotFoundException;
import com.logicgate.employee.model.Employee;
import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.userrole.exception.UserRoleNotFoundException;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee) throws UserRoleNotFoundException, EmployeeNotFoundException, AppUserNotFoundException;
    Employee fetchEmployeeById(Long id) throws EmployeeNotFoundException;
    Employee fetchEmployeeByCode(String employeeCode) throws EmployeeNotFoundException;
    List<Employee> fetchAllEmployeeOrByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(Integer pageNumber, String searchKey);
    Employee fetchEmployeeByUsernameOrMobileOEmail(String searchKey) throws EmployeeNotFoundException;
    List<Employee> fetchEmployeeByMaritalStatus(Integer pageNumber, MaritalStatus maritalStatus);
    List<Employee> fetchEmployeeByJobDesignation(String searchKey, Integer pageNumber) throws JobDesignationNotFoundException;
    List<Employee> fetchEmployeeByGender(Integer pageNumber, Gender gender);
    List<Employee> fetchEmployeeByStatus(Integer pageNumber, EmployeeStatus employeeStatus);
    Employee updateEmployee(Employee employee, Long id) throws EmployeeNotFoundException;
    void deleteEmployee(Long id) throws EmployeeNotFoundException;
    void deleteAllEmployees();
}
