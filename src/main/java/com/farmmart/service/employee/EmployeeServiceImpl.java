package com.farmmart.service.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.employee.EmployeeNotFoundException;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    @Override
    public Employee findEmployeeById(Long id) throws EmployeeNotFoundException {
        Employee employee=employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException("Employee Not Found"));

        return employee;
    }

    @Override
    public List<Employee> findEmployeeByFirstName(String firstName)  {
        List<Employee> employees=employeeRepository.findByFirstName(firstName);

        return employees;
    }

    @Override
    public List<Employee> findEmployeeByLastName(String lastName)  {
        List<Employee> employees=employeeRepository.findByLastName(lastName);

        return employees;
    }

    @Override
    public List<Employee> findEmployeeByGender(Gender gender) {

        List<Employee> employees=employeeRepository.findByGender(gender);

        return employees;
    }

    @Override
    public Employee findEmployeeByUsername(AppUser appUser,String username) {
        appUser=appUserRepository.findByUserName(username);

        Employee employee=employeeRepository.findByUsername(appUser);

        return employee;
    }

    @Override
    public List<Employee> findAllEmployee() {

        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployeeDetails(Employee employee, Long id) throws EmployeeNotFoundException {
        Employee savedEmployee=employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException("Employee Not Found"));

        if (Objects.nonNull(employee.getFirstName())||!"".equalsIgnoreCase(employee.getFirstName())){
            savedEmployee.setFirstName(employee.getFirstName());
        }if (Objects.nonNull(employee.getLastName())||!"".equalsIgnoreCase(employee.getLastName())){
            savedEmployee.setLastName(employee.getLastName());
        }if (Objects.nonNull(employee.getOtherNames())||!"".equalsIgnoreCase(employee.getOtherNames())){
            savedEmployee.setOtherNames(employee.getOtherNames());
        }if (Objects.nonNull(employee.getNextOfKin())||!"".equalsIgnoreCase(employee.getNextOfKin())){
            savedEmployee.setNextOfKin(employee.getNextOfKin());
        }if (Objects.nonNull(employee.getRelationshipWithNextOfKin())){
            savedEmployee.setRelationshipWithNextOfKin(employee.getRelationshipWithNextOfKin());
        }

        return employeeRepository.save(savedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeNotFoundException {
        employeeRepository.deleteById(id);

        Optional<Employee> optionalEmployee=employeeRepository.findById(id);

        if (optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("Employee is Not Deleted ");
        }

    }

    @Override
    public void deleteAllEmployee() {

        employeeRepository.deleteAll();
    }
}
