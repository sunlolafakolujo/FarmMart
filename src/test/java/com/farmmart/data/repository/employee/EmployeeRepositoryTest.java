package com.farmmart.data.repository.employee;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.employee.EmployeeNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.RelationshipWithNextOfKin;
import com.farmmart.data.model.staticdata.Status;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.repository.address.AddressRepository;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.localgovernment.LocalGovernmentRepository;
import com.farmmart.data.repository.userrole.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    Employee employee;

    AppUser appUser;

    UserRole userRole;

    LocalGovernment localGovernment;

    Address address;

    @BeforeEach
    void setUp() {
        employee=new Employee();
        appUser=new AppUser();
        userRole=new UserRole();
        localGovernment=new LocalGovernment();
        address=new Address();
    }

    @Test
    void testThatYouCanSaveEmployee(){
        localGovernment=localGovernmentRepository.findByLocalGovernmentName("Amuwo Odofin");

        address.setLocalGovernment(localGovernment);
        address.setStreetNumber("Block 433, Flat 1");
        address.setStreetName("Amuwo Odofin Housing Estate");
        address.setCity("Mile 2");
        address.setPostZipCode("111001");
        address.setLandMark("Crystal Palace Estate");

        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        userRole=userRoleRepository.findByRoleName("ROLE_EMPLOYEE");
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);

        appUser.setAddresses(addresses);
        appUser.setUsername("SunFak");
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setUserType(UserType.EMPLOYEE);
        appUser.setPhone("08097545671");
        appUser.setUserRoles(userRoles);
        appUser.setEmail("fakolujos@yahoo.com");

        employee.setDob(LocalDate.parse("2001-04-12"));
        employee.setAppUser(appUser);
        employee.setEmployeeStatus(Status.STAFF);
        employee.setGender(Gender.MALE);
        employee.setFirstName("Sunlola");
        employee.setLastName("Fakolujo");
        employee.setNextOfKin("Abosede Fakolujo");
        employee.setRelationshipWithNextOfKin(RelationshipWithNextOfKin.SPOUSE);
        employee.setOtherNames("Emmanuel");
        employee.setHiredDate(LocalDate.parse("2005-10-10"));

        assertDoesNotThrow(()->employeeRepository.save(employee));

        log.info("Employee repo {}",employee);
    }

    @Test
    void testThatYouCanFindEmployeeById() throws EmployeeNotFoundException {
        Long id=2L;
        employee=employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee Not Found"));

        assertEquals(id,employee.getId());

        log.info("Employee {}",employee);
    }

    @Test
    void testThatYouCanFindEmployeeByFirstName() throws EmployeeNotFoundException {
        String firstNamesLike="Heph";

        List<Employee> employees=employeeRepository.findByFirstName(firstNamesLike);

        if (employees==null){
            throw new EmployeeNotFoundException("Employee Not Found");
        }

        log.info("Employee with first name like {}",employees);
    }

    @Test
    void testThatYouCanFindEmployeeByLastName() throws EmployeeNotFoundException {
        String lastNamesLike="fak";

        List<Employee> employee=employeeRepository.findByLastName(lastNamesLike);

        if (employee.isEmpty()){
            throw new EmployeeNotFoundException("Employee Not Found");
        }

        log.info("Employee with first name like Akin{}",employee);
    }

    @Test
    void testThatYouCanFindEmployeeByGender() throws EmployeeNotFoundException {
        Gender gender=Gender.MALE;

        List<Employee> employee=employeeRepository.findByGender(gender);

        if (employee.isEmpty()){
            throw new EmployeeNotFoundException("Employee Not Found");
        }

        log.info("Employee with first name like Akin{}",employee);
    }

    @Test
    void testThatYouCanFindEmployeeByUsername() {
        String username="AkinSquare";
        appUser=appUserRepository.findByUserName(username);

        employee=employeeRepository.findByUsername(appUser);

        log.info("Employee {}",employee);
    }

    @Test
    void testThatYouCanFindAllEmployee(){
        List<Employee> employees=employeeRepository.findAll();

        log.info("All Employees {}",employees);
    }

    @Test
    void testThatYouCanUpdateEmployeeDetails() throws EmployeeNotFoundException {
        Long id=1L;
        employee=employeeRepository.findById(id).orElseThrow(()->new EmployeeNotFoundException("Employee Not Found"));
        String otherNames="PamPam";
        employee.setOtherNames(otherNames);

        assertDoesNotThrow(()->employeeRepository.save(employee));

        assertEquals(otherNames,employee.getOtherNames());

        log.info("New Employee other name {}",employee.getOtherNames());
    }

    @Test
    @Rollback(value = false)
    void testThatYouCanDeleteEmployeeById() throws EmployeeNotFoundException {
        Long id=2L;
        employeeRepository.deleteById(id);

        Optional<Employee> optionalEmployee=employeeRepository.findById(id);

        if (optionalEmployee.isPresent()){
            throw new EmployeeNotFoundException("Employee Not Deleted");
        }
    }

    @Test
    @Rollback(value = false)
    void testThatYouCanDeleteAllEmployee(){
        employeeRepository.deleteAll();
    }

}