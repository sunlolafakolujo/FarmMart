package com.farmmart.service.customer;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.customer.CustomerNotFoundException;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.customer.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer saveCustomer(Customer customer) {

        customer.getAppUser().setPassword(passwordEncoder.encode(customer.getAppUser().getPassword()));

        customer.setCustomerCode("CUS".concat(UUID.randomUUID().toString().replace("-", "").toUpperCase()));

        return customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerById(Long id) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(id)
                .orElseThrow(()-> new CustomerNotFoundException("Customer Not Found"));

        return customer;
    }

    @Override
    public List<Customer> findCustomerByFirstNameLike(String firstName) {

        return customerRepository.findCustomerByFirstNameLike(firstName);
    }

    @Override
    public List<Customer> findCustomerByLastNameLike(String lastName) {

        List<Customer> customerLastName=customerRepository.findCustomerByLastNameLike(lastName);

        return customerLastName;
    }

    @Override
    public List<Customer> findCustomerByGender(Gender gender) {
        return customerRepository.findCustomerByGender(gender);
    }

    @Override
    public List<Customer> findCustomerByAgeRange(AgeRange ageRange) {
        return customerRepository.findCustomerByAgeRange(ageRange);
    }

    @Override
    public List<Customer> findAllCustomers(Integer limit) {
        return customerRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Customer findCustomerByUsername(AppUser appUser, String username) {
        appUser=appUserRepository.findByUserName(username);

        return customerRepository.findCustomerByUsername(appUser);
    }

    @Override
    public Customer updateCustomer(Customer customer, Long id) throws CustomerNotFoundException {
        Customer savedCustomer=customerRepository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not Found"));

        if (Objects.nonNull(customer.getFirstName())||!"".equalsIgnoreCase(customer.getFirstName())){
            savedCustomer.setFirstName(customer.getFirstName());
        }if (Objects.nonNull(customer.getLastName())||!"".equalsIgnoreCase(customer.getLastName())){
            savedCustomer.setLastName(customer.getLastName());
        }
        return customerRepository.save(savedCustomer);
    }

    @Override
    public void deleteCustomerById(Long id) throws CustomerNotFoundException {

        customerRepository.deleteById(id);

        Optional<Customer> optionalCustomer=customerRepository.findById(id);

        if (optionalCustomer.isPresent()){
            throw new CustomerNotFoundException("Customer is Not Deleted");
        }
    }

    @Override
    public void deleteAllCustomers() {

        customerRepository.deleteAll();
    }
}
