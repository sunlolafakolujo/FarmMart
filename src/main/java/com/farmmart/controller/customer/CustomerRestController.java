package com.farmmart.controller.customerrestcontroller;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.customer.*;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.userrole.UserRoleNotFoundException;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.customer.CustomerServiceImpl;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/customer")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerServiceImpl customerService;

    private final AppUserDetailService appUserService;

    private final UserRoleServiceImpl userRoleService;

    private final ModelMapper modelMapper;

    @PostMapping("/registerCustomer")
    public ResponseEntity<NewCustomer> registerCustomer(@Valid @RequestBody NewCustomer newCustomer)
                                                        throws UserRoleNotFoundException {

        newCustomer.getAppUser().setUserType(UserType.CUSTOMER);

        UserRole userRole=userRoleService.findUserRoleByName("ROLE_CUSTOMER");

        List<UserRole> userRoles=new ArrayList<>();

        userRoles.add(userRole);

        newCustomer.getAppUser().setUserRoles(userRoles);

        Customer customer=modelMapper.map(newCustomer,Customer.class);

        Customer saveCustomer=customerService.saveCustomer(customer);

        NewCustomer post=modelMapper.map(saveCustomer,NewCustomer.class);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/findCustomerById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(value = "id") Long id) throws CustomerNotFoundException {
        Customer customer=customerService.findCustomerById(id);

        CustomerDto customerDto=convertCustomerToDto(customer);

        return ResponseEntity.ok().body(customerDto);
    }

    @GetMapping("/findCustomerByFirstName/{fName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDto>> getCustomerByNameLike(@PathVariable(value = "fName") String firstName){

        return ResponseEntity.ok().body(customerService.findCustomerByFirstNameLike(firstName)
                .stream()
                .map(this::convertCustomerToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findCustomerByLastName/{lName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDto>> getCustomerByLastNameLike(@PathVariable(value = "lName") String lastName){

        return ResponseEntity.ok().body(customerService.findCustomerByLastNameLike(lastName)
                .stream()
                .map(this::convertCustomerToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findCustomerByGender/{gender}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDto>> getCustomerByGender(@PathVariable(value = "gender") Gender gender){

        return ResponseEntity.ok().body(customerService.findCustomerByGender(gender)
                .stream()
                .map(this::convertCustomerToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findCustomerByAgeRange/{ageRange}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDto>> getCustomerByAgeRange(@PathVariable(value = "ageRange") AgeRange ageRange){

        return ResponseEntity.ok().body(customerService.findCustomerByAgeRange(ageRange)
                .stream()
                .map(this::convertCustomerToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findAllCustomer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDto>> getAllCustomers(){

        return ResponseEntity.ok().body(customerService.findAllCustomers()
                .stream()
                .map(this::convertCustomerToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findCustomerByUsername/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CustomerDto> getCustomerByUsername(@PathVariable(value = "username") String username) throws AppUserNotFoundException {
        AppUser appUser=appUserService.findUserByUsername(username);

        Customer customer=customerService.findCustomerByUsername(appUser,username);

        CustomerDto customerDto=modelMapper.map(customer,CustomerDto.class);

        return ResponseEntity.ok().body(customerDto);
    }

    @PutMapping("/updateCustomerById/{id}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<ModifyCustomer> updateCustomerById(@Valid @RequestBody ModifyCustomer modifyCustomer,
                                                             @PathVariable(value = "id") Long id) throws CustomerNotFoundException {
        Customer customer=modelMapper.map(modifyCustomer,Customer.class);

        Customer updateCustomer=customerService.updateCustomer(customer,id);

        ModifyCustomer updatedCustomer=modelMapper.map(updateCustomer,ModifyCustomer.class);

        return ResponseEntity.ok().body(updatedCustomer);
    }

    @DeleteMapping("/deleteCustomerById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCustomerById(@PathVariable(value = "id") Long id) throws CustomerNotFoundException {
        customerService.deleteCustomerById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllCustomer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllCustomers(){
        customerService.deleteAllCustomers();

        return ResponseEntity.noContent().build();
    }

    private CustomerDto convertCustomerToDto(Customer customer){
        CustomerDto customerDto=new CustomerDto();

        customerDto.setId(customer.getId());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setGender(customer.getGender());
        customerDto.setAgeRange(customer.getAgeRange());
        customerDto.setPhone(customer.getAppUser().getPhone());
        customerDto.setAddresses(customer.getAppUser().getAddresses());

        return customerDto;
    }
}
