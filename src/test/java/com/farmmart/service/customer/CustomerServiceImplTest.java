package com.farmmart.service.customer;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.customer.CustomerNotFoundException;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CustomerService customerService=new CustomerServiceImpl();

    Customer customer;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer=new Customer();
        appUser=new AppUser();
    }

    @Test
    void testThatYouCanMockSaveCustomerMock() {
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        customerService.saveCustomer(customer);

        ArgumentCaptor<Customer> customerArgumentCaptor=ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerRepository,Mockito.times(1)).save(customerArgumentCaptor.capture());

        Customer capturedCustomer=customerArgumentCaptor.getValue();

        assertEquals(capturedCustomer,customer);
    }

    @Test
    void testThatYouCanMockFindCustomerByIdMethod() throws CustomerNotFoundException {
        Long id=1L;
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerService.findCustomerById(id);

        Mockito.verify(customerRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindCustomerByFirstNameLikeMethod() {
        String firstName="Buko";

        List<Customer> customers=new ArrayList<>();

        Mockito.when(customerRepository.findCustomerByFirstNameLike(firstName)).thenReturn(customers);

        customerService.findCustomerByFirstNameLike(firstName);

        Mockito.verify(customerRepository,Mockito.times(1)).findCustomerByFirstNameLike(firstName);
    }

    @Test
    void testThatYouCanMockFindCustomerByLastNameLikeMethod() {
        String lastName="fako";

        List<Customer> customers=new ArrayList<>();

        Mockito.when(customerRepository.findCustomerByLastNameLike(lastName)).thenReturn(customers);

        customerService.findCustomerByLastNameLike(lastName);

        Mockito.verify(customerRepository,Mockito.times(1)).findCustomerByLastNameLike(lastName);
    }

    @Test
    void findCustomerByGender() {
        Gender gender= Gender.FEMALE;

        List<Customer> customers=new ArrayList<>();

        Mockito.when(customerRepository.findCustomerByGender(gender)).thenReturn(customers);

        customerService.findCustomerByGender(gender);

        Mockito.verify(customerRepository,Mockito.times(1)).findCustomerByGender(gender);
    }

    @Test
    void testThatYouCanMockFindCustomerByAgeRangeMethod() {

        AgeRange ageRange=AgeRange.SIXTY_TO_SEVENTY_NINE;

        List<Customer> customers=new ArrayList<>();

        Mockito.when(customerRepository.findCustomerByAgeRange(ageRange)).thenReturn(customers);

        customerService.findCustomerByAgeRange(ageRange);

        Mockito.verify(customerRepository,Mockito.times(1)).findCustomerByAgeRange(ageRange);
    }

    @Test
    void findAllCustomers() {

        List<Customer> customers=new ArrayList<>();

        Mockito.when(customerRepository.findAll()).thenReturn(customers);

        customerService.findAllCustomers(10);

        Mockito.verify(customerRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockFindCustomerByUsernameMethod() {

        String username="AkinEmma";
        appUser=appUserRepository.findByUserName(username);

        Mockito.when(customerRepository.findCustomerByUsername(appUser)).thenReturn(customer);

        customerService.findCustomerByUsername(appUser,username);

        Mockito.verify(customerRepository,Mockito.times(1)).findCustomerByUsername(appUser);
    }

    @Test
    void testThatYouCanMockUpdatedMethod() throws CustomerNotFoundException {
        Long id=1L;
        Mockito.when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerService.updateCustomer(customer,id);

        Mockito.verify(customerRepository,Mockito.times(1)).save(customer);
    }

    @Test
    void testThatYouCanMockDeleteCustomerByIdMethod() throws CustomerNotFoundException {
        Long id=2L;

        doNothing().when(customerRepository).deleteById(id);

        customerService.deleteCustomerById(id);

        verify(customerRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllCustomersMethod() {
        doNothing().when(customerRepository).deleteAll();

        customerService.deleteAllCustomers();

        verify(customerRepository,times(1)).deleteAll();
    }
}