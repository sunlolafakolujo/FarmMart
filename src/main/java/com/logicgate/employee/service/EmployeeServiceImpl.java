package com.logicgate.employee.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.employee.exception.EmployeeNotFoundException;
import com.logicgate.employee.model.Employee;
import com.logicgate.employee.repository.EmployeeRepository;
import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.jobdesignation.repository.JobDesignationRepository;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.UserRole;
import com.logicgate.userrole.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private JobDesignationRepository jobDesignationRepository;

    @Override
    public Employee addEmployee(Employee employee) throws UserRoleNotFoundException,
            EmployeeNotFoundException, AppUserNotFoundException {
        Optional<AppUser> savedUser=appUserRepository.findUserByUsernameOrEmailOrMobile(employee.getAppUser().getUsername(),
                employee.getAppUser().getEmail(),employee.getAppUser().getMobile());
        if (savedUser.isPresent()){
            throw new AppUserNotFoundException("Username or Email or Mobile is already exist");
        }if (!validatePhoneNumber(employee)){
            throw new EmployeeNotFoundException("Mobile phone must be in one of these formats: " +
                    "10 or 11 digit, 0000 000 0000, 000 000 0000, 000-000-0000, 000-000-0000 ext0000");
        }if (!employee.getAppUser().getPassword().equals(employee.getAppUser().getConfirmPassword())){
            throw new AppUserNotFoundException("Password does not match");
        }
        employee.getAppUser().setUserCode("USER".concat(String.valueOf(new Random().nextInt(100000))));
        UserRole userRole=userRoleRepository.findByUserRoleName("EMPLOYEE")
                .orElseThrow(()->new UserRoleNotFoundException("Role Not Found"));
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);
        employee.getAppUser().setIsEnabled(true);
        employee.getAppUser().setUserType(UserType.EMPLOYEE);
        employee.setEmployeeCode("EMP".concat(String.valueOf(new Random().nextInt(100000))));
        employee.getAppUser().setPassword(passwordEncoder.encode(employee.getAppUser().getPassword()));
        employee.setAge(Period.between(employee.getDateOfBirth(), LocalDate.now()).getYears());
        employee.getAppUser().setUserRoles(userRoles);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee fetchEmployeeById(Long id) throws EmployeeNotFoundException {
        return employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException("Employee with ID "+id+" Not Found"));
    }

    @Override
    public Employee fetchEmployeeByCode(String employeeCode) throws EmployeeNotFoundException {
        return employeeRepository.findEmployeeByCode(employeeCode)
                .orElseThrow(()->new EmployeeNotFoundException("Employee Code "+employeeCode+" Not Found"));
    }

    @Override
    public List<Employee> fetchAllEmployeeOrByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(Integer pageNumber,
                                                                                             String searchKey) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        if (searchKey.equals("")){
           return employeeRepository.findAll(pageable).toList();
       }else {
            return employeeRepository.findByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(searchKey,searchKey,
                    searchKey,searchKey,pageable);
        }
    }

    @Override
    public Employee fetchEmployeeByUsernameOrMobileOEmail(String searchKey) throws EmployeeNotFoundException {
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new EmployeeNotFoundException("Employee with "+searchKey+" Not Found"));
        return employeeRepository.findByAppUser(appUser);
    }

    @Override
    public List<Employee> fetchEmployeeByMaritalStatus(Integer pageNumber, MaritalStatus maritalStatus) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (maritalStatus.equals("")){
            return employeeRepository.findAll(pageable).toList();
        }else {
            return employeeRepository.findEmployeeByMaritalStatus(maritalStatus, pageable);
        }
    }

    @Override
    public List<Employee> fetchEmployeeByJobDesignation(String searchKey, Integer pageNumber) throws JobDesignationNotFoundException {
        Pageable pageable=PageRequest.of(pageNumber,10);
        JobDesignation jobDesignation=jobDesignationRepository.findByJobDesignationCodeOrJobTitle(searchKey,searchKey)
                .orElseThrow(()->new JobDesignationNotFoundException("Job Designation "+searchKey+" Not Found"));
        if (searchKey.equals("")){
            return employeeRepository.findAll(pageable).toList();
        }else {
            return employeeRepository.findByJobDesignation(jobDesignation, pageable);
        }
    }

    @Override
    public List<Employee> fetchEmployeeByGender(Integer pageNumber, Gender gender) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (gender.equals("")){
            return employeeRepository.findAll(pageable).toList();
        }else {
            return employeeRepository.findEmployeeByGender(gender, pageable);
        }
    }

    @Override
    public List<Employee> fetchEmployeeByStatus(Integer pageNumber, EmployeeStatus employeeStatus) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (employeeStatus.equals("")){
            return employeeRepository.findAll(pageable).toList();
        }else {
            return employeeRepository.findByEmployeeStatus(employeeStatus, pageable);
        }
    }

    @Override
    public Employee updateEmployee(Employee employee, Long id) throws EmployeeNotFoundException {
        Employee savedEmployee=employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException("Employee with ID "+id+" Not Found"));
        if (Objects.nonNull(employee.getFirstName()) && !"".equalsIgnoreCase(employee.getFirstName())){
            savedEmployee.setFirstName(employee.getFirstName());
        }if (Objects.nonNull(employee.getLastName()) && !"".equalsIgnoreCase(employee.getLastName())){
            savedEmployee.setLastName(employee.getLastName());
        }if (Objects.nonNull(employee.getOtherNames()) && !"".equalsIgnoreCase(employee.getOtherNames())){
            savedEmployee.setOtherNames(employee.getOtherNames());
        }if (Objects.nonNull(employee.getNextOfKinName()) && !"".equalsIgnoreCase(employee.getNextOfKinName())){
            savedEmployee.setNextOfKinName(employee.getNextOfKinName());
        }if (Objects.nonNull(employee.getRelationshipWithNextOfKin()) && !"".equals(employee.getRelationshipWithNextOfKin())){
            savedEmployee.setRelationshipWithNextOfKin(employee.getRelationshipWithNextOfKin());
        }if (Objects.nonNull(employee.getPicture()) && !"".equals(employee.getPicture())){
            savedEmployee.setPicture(employee.getPicture());
        }if (Objects.nonNull(employee.getJobDesignation()) && !"".equals(employee.getJobDesignation())){
            savedEmployee.setJobDesignation(employee.getJobDesignation());
        }if (Objects.nonNull(employee.getMaritalStatus()) && !"".equals(employee.getMaritalStatus())){
            savedEmployee.setMaritalStatus(employee.getMaritalStatus());
        }if (Objects.nonNull(employee.getEmployeeStatus()) && !"".equals(employee.getEmployeeStatus())){
            savedEmployee.setEmployeeStatus(employee.getEmployeeStatus());
        }if (Objects.nonNull(employee.getAppUser().getContact().getHouseNumber()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getHouseNumber())){
            savedEmployee.getAppUser().getContact().setHouseNumber(employee.getAppUser().getContact().getHouseNumber());
        }if (Objects.nonNull(employee.getAppUser().getContact().getStreetName()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getStreetName())){
            savedEmployee.getAppUser().getContact().setStreetName(employee.getAppUser().getContact().getStreetName());
        }if (Objects.nonNull(employee.getAppUser().getContact().getCity()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getCity())){
            savedEmployee.getAppUser().getContact().setCity(employee.getAppUser().getContact().getCity());
        }if (Objects.nonNull(employee.getAppUser().getContact().getLandmark()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getLandmark())){
            savedEmployee.getAppUser().getContact().setLandmark(employee.getAppUser().getContact().getLandmark());
        }if (Objects.nonNull(employee.getAppUser().getContact().getStateProvince()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getStateProvince())){
            savedEmployee.getAppUser().getContact().setStateProvince(employee.getAppUser().getContact().getStateProvince());
        }if (Objects.nonNull(employee.getAppUser().getContact().getCountry()) &&
                !"".equalsIgnoreCase(employee.getAppUser().getContact().getCountry())){
            savedEmployee.getAppUser().getContact().setCountry(employee.getAppUser().getContact().getCountry());
        }
        return employeeRepository.save(savedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) throws EmployeeNotFoundException {
        if (employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
        }else {
            throw new EmployeeNotFoundException("Employee ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    private static boolean validatePhoneNumber(Employee employee) {
        // validate phone numbers of format "1234567890"
        if (employee.getAppUser().getMobile().matches("\\d{10}")) {
            return true;
        }
        // validate phone numbers of format "12345678901"
        else if (employee.getAppUser().getMobile().matches("\\d{11}")) {
            return true;
        }
        // validating phone number with -, . or spaces
        else if (employee.getAppUser().getMobile().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }
        // validating phone number with extension length from 3 to 5
        else if (employee.getAppUser().getMobile().matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        }
        // validating phone number where area code is in braces ()
        else if (employee.getAppUser().getMobile().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }    // Validation for India numbers
        else if (employee.getAppUser().getMobile().matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) {
            return true;
        } else if (employee.getAppUser().getMobile().matches("\\(\\d{5}\\)-\\d{3}-\\d{3}")) {
            return true;
        } else if (employee.getAppUser().getMobile().matches("\\(\\d{4}\\)-\\d{3}-\\d{3}")){
            return true;
        }    // return false if nothing matches the input
        else {
            return false;
        }
    }
}
