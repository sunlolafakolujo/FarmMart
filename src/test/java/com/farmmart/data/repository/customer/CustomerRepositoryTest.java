package com.farmmart.data.repository.customer;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.customer.CustomerNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Slf4j
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class CustomerRepositoryTest {

    @Autowired
    private LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    LocalGovernment localGovernment;

    Address address;

    UserRole userRole;

    AppUser appUser;
    Customer customer;

    @BeforeEach
    void setUp() {
        localGovernment=new LocalGovernment();
        address=new Address();
        userRole=new UserRole();
        appUser=new AppUser();
        customer=new Customer();
    }

    @Test
    void testThatYouCanSaveCustomer(){
        Long id=8L;
        localGovernment=localGovernmentRepository.findById(id).orElseThrow();

        address.setLocalGovernment(localGovernment);
        address.setStreetNumber("Block 5 Bar Beach Tower");
        address.setStreetName("Bishop Oluwole Street");
        address.setCity("Victoria Island");
        address.setPostZipCode("");
        address.setLandMark("Bar Beach Bus Stop");

        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        userRole=userRoleRepository.findByRoleName("Role_CUSTOMER");
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);


        appUser.setUsername("StevenEse");
        appUser.setPassword(passwordEncoder.encode("123456"));
        appUser.setPhone("08087645621");
        appUser.setEmail("stevecode@Yahoo.com");
        appUser.setUserType(UserType.CUSTOMER);
        appUser.setAddresses(addresses);
        appUser.setUserRoles(userRoles);

        customer.setFirstName("Steven");
        customer.setLastName("Esebere");
        customer.setGender(Gender.MALE);
        customer.setAgeRange(AgeRange.FORTY_TO_FIFTY_NINE);
        customer.setAppUser(appUser);

        assertDoesNotThrow(()->customerRepository.save(customer));

        log.info("Customer repo {}",customer);
    }

    @Test
    void tesThatYouCanFindCustomerById() throws CustomerNotFoundException {
        Long id=1L;
        customer=customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer Not Found"));

        assertEquals(id,customer.getId());

        log.info("Customer {}",customer.getFirstName());
    }

    @Test
    void testThatYouCanFindCustomerByFirstName() throws CustomerNotFoundException {
        String firstNameLike="Buko";

        List<Customer> customers=customerRepository.findCustomerByFirstNameLike(firstNameLike);

        if (customers.isEmpty()){
            throw new CustomerNotFoundException("Customer with first name "+firstNameLike+" Not Found");
        }

        log.info("First name like Buko {}",customers);

        log.info("Size {}",customers.size());
    }

    @Test
    void testThatYouCanFindCustomerByLastName() throws CustomerNotFoundException {

        String lastNameLike="Emma";
        List<Customer> customers=customerRepository.findCustomerByLastNameLike(lastNameLike);

        if (customers.isEmpty()){
            throw new CustomerNotFoundException("Customer with First Name "+lastNameLike+" Not Found");
        }

        log.info("Last name like Emma {}",customers);

        log.info("Size {}",customers.size());
    }

    @Test
    void testThatYouCanFindCustomerByAgeRange() throws CustomerNotFoundException {
        AgeRange ageRange=AgeRange.FORTY_TO_FIFTY_NINE;

        List<Customer> customers=customerRepository.findCustomerByAgeRange(ageRange);

        if (customers.isEmpty()){
            throw new CustomerNotFoundException("Customer with Age Range "+ageRange+" Not Found");
        }

        log.info("Customer with Age Range 40-59 {}",customers);
    }

    @Test
    void testThatYouCanFindCustomerByGender() throws CustomerNotFoundException {
        Gender gender=Gender.MALE;

        List<Customer> customers=customerRepository.findCustomerByGender(gender);

        if (customers.isEmpty()){
            throw new CustomerNotFoundException("Customer gender "+gender+" Not Found");
        }

        log.info("Customers with gender Male {}",customers);
    }

    @Test
    void testThatYouCanFindAllCustomers(){
        List<Customer> customers=customerRepository.findAll();

        log.info("All Customers {}",customers);
    }

    @Test
    void testThatYouCanFindCustomerByUsername(){
        String username="AkinEmma";
        appUser=appUserRepository.findByUserName(username);

        customer=customerRepository.findCustomerByUsername(appUser);

        log.info("Customer with username AkinEmma {}",customer);

    }

    @Test
    void testThatYouCanUpdateCustomerDetails(){
        Long id=1L;
        customer=customerRepository.findById(id).orElseThrow();

        appUser=appUserRepository.findByUserName("BukolaFako");

        appUser.setPassword("Akinpelumi@04071974");

        customer.setAppUser(appUser);

        assertDoesNotThrow(()->customerRepository.save(customer));

        log.info("Updated Customer {}",customer.getAppUser().getPassword());
    }


    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteCustomerById() throws CustomerNotFoundException {

        Long id=2L;
        customerRepository.deleteById(id);

        Optional<Customer> optionalCustomer=customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            throw new CustomerNotFoundException("Customer is Not Deleted");
        }
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteAllCustomers(){
        customerRepository.deleteAll();
    }
}