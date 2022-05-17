package com.farmmart.service.customer;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.customer.CustomerNotFoundException;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;

import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Customer findCustomerById(Long id) throws CustomerNotFoundException;
    List<Customer> findCustomerByFirstNameLike(String firstName);
    List<Customer> findCustomerByLastNameLike(String lastName);
    List<Customer> findCustomerByGender(Gender gender);
    List<Customer> findCustomerByAgeRange(AgeRange ageRange);
    List<Customer> findAllCustomers(Integer limit);
    Customer findCustomerByUsername(AppUser appUser, String username);
    Customer updateCustomer(Customer customer, Long id) throws CustomerNotFoundException;
    void deleteCustomerById(Long id) throws CustomerNotFoundException;
    void deleteAllCustomers();
}
