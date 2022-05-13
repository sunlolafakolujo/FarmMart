package com.farmmart.controller.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.employee.*;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.userrole.UserRoleNotFoundException;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.employee.EmployeeServiceImpl;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/employee")
@RequiredArgsConstructor
public class EmployeeRestController {

    private final EmployeeServiceImpl employeeService;

    private final AppUserDetailService appUserService;

    private final UserRoleServiceImpl userRoleService;

    private final ModelMapper modelMapper;

    @PostMapping("/registerEmployee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewEmployee> registerEmployee(@Valid @RequestBody NewEmployee newEmployee) throws UserRoleNotFoundException {

        newEmployee.getAppUser().setUserType(UserType.EMPLOYEE);

        UserRole userRole= userRoleService.findUserRoleByName("ROLE_EMPLOYEE");

        List<UserRole> userRoles=new ArrayList<>();

        userRoles.add(userRole);

        newEmployee.getAppUser().setUserRoles(userRoles);

        newEmployee.setAge(Period.between(newEmployee.getDob(), LocalDate.now()).getYears());

        Employee employee=modelMapper.map(newEmployee,Employee.class);

        Employee saveEmployee=employeeService.saveEmployee(employee);

        NewEmployee post=modelMapper.map(saveEmployee,NewEmployee.class);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/findEmployeeById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable(value = "id") Long id) throws EmployeeNotFoundException {
        Employee employee=employeeService.findEmployeeById(id);

        EmployeeDto employeeDto=convertEmployeeToDto(employee);

        return ResponseEntity.ok().body(employeeDto);
    }

    @GetMapping("/findEmployeeByFirstName/{fName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByFirstName(@PathVariable(value = "fName") String firstName){

        return ResponseEntity.ok().body(employeeService.findEmployeeByFirstName(firstName)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findEmployeeByLastName/{lName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByLastName(@PathVariable(value = "lName") String lastName){

        return ResponseEntity.ok().body(employeeService.findEmployeeByLastName(lastName)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findEmployeeByGender/{gender}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByGender(@PathVariable(value = "gender") Gender gender){

        return ResponseEntity.ok().body(employeeService.findEmployeeByGender(gender)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findEmployeeByUsername/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EmployeeDto> getEmployeeByUsername(@PathVariable(value = "username") String username) throws AppUserNotFoundException {
        AppUser appUser=appUserService.findUserByUsername(username);

        Employee employee=employeeService.findEmployeeByUsername(appUser,username);

        EmployeeDto employeeDto=convertEmployeeToDto(employee);

        return ResponseEntity.ok().body(employeeDto);
    }

    @GetMapping("/findAllEmployees")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(){

        return ResponseEntity.ok().body(employeeService.findAllEmployee()
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateEmployeeById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<ModifyEmployee> updateEmployeDetailsById(@Valid @RequestBody ModifyEmployee modifyEmployee,
                                                                   @PathVariable(value = "id") Long id) throws EmployeeNotFoundException {

        Employee employee=modelMapper.map(modifyEmployee,Employee.class);

        Employee updateEmployee=employeeService.updateEmployeeDetails(employee,id);

        ModifyEmployee postUpdate=modelMapper.map(updateEmployee,ModifyEmployee.class);

        return ResponseEntity.ok().body(postUpdate);
    }

    @PutMapping("/updateEmployee/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyEmployeeAdmin> updateEmployeDetailsByIdAdmin(@Valid @RequestBody ModifyEmployeeAdmin modifyEmployeeAdmin,
                                                                   @PathVariable(value = "id") Long id) throws EmployeeNotFoundException {

        Employee employee=modelMapper.map(modifyEmployeeAdmin,Employee.class);

        Employee updateEmployee=employeeService.updateEmployeeDetails(employee,id);

        ModifyEmployeeAdmin postUpdate=modelMapper.map(updateEmployee,ModifyEmployeeAdmin.class);

        return ResponseEntity.ok().body(postUpdate);
    }

    @DeleteMapping("/deleteEmployeeById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteEmployeeById(@PathVariable(value = "id") Long id) throws EmployeeNotFoundException {

        employeeService.deleteEmployeeById(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllEmployee")
    public ResponseEntity<?> deleteAllEmployee() {

        employeeService.deleteAllEmployee();

        return ResponseEntity.noContent().build();
    }


    private EmployeeDto convertEmployeeToDto(Employee employee){
        EmployeeDto employeeDto=new EmployeeDto();

        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setOtherNames(employee.getOtherNames());
        employeeDto.setDob(employee.getDob());
        employeeDto.setAge(employee.getAge());
        employeeDto.setGender(employee.getGender());
        employeeDto.setNextOfKin(employee.getNextOfKin());
        employeeDto.setRelationshipWithNextOfKin(employee.getRelationshipWithNextOfKin());
        employeeDto.setHiredDate(employee.getHiredDate());
        employeeDto.setEndDate(employee.getEndDate());
        employeeDto.setPhone(employee.getAppUser().getPhone());
        employeeDto.setEmail(employee.getAppUser().getEmail());
        employeeDto.setAddresses(employee.getAppUser().getAddresses());

        return employeeDto;
    }
}
