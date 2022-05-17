package com.farmmart.service.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.employee.EmployeeNotFoundException;
import com.farmmart.data.model.staticdata.Gender;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee) throws EmployeeNotFoundException;
    Employee findEmployeeById(Long id) throws EmployeeNotFoundException;
    List<Employee> findEmployeeByFirstName(String firstName) throws EmployeeNotFoundException;
    List<Employee> findEmployeeByLastName(String lastName) throws EmployeeNotFoundException;
    List<Employee> findEmployeeByGender(Gender gender) throws EmployeeNotFoundException;
    Employee findEmployeeByUsername(AppUser appUser,String username);
    List<Employee> findAllEmployee(Integer limit);
    Employee updateEmployeeDetails(Employee employee,Long id) throws EmployeeNotFoundException;
    void deleteEmployeeById(Long id) throws EmployeeNotFoundException;
    void deleteAllEmployee();

}
