package com.logicgate.employee.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.employee.exception.EmployeeNotFoundException;
import com.logicgate.employee.model.Employee;
import com.logicgate.employee.model.EmployeeDto;
import com.logicgate.employee.model.NewEmployee;
import com.logicgate.employee.model.UpdateEmployee;
import com.logicgate.employee.service.EmployeeService;
import com.logicgate.event.RegistrationEvent;
import com.logicgate.image.model.Picture;
import com.logicgate.jobdesignation.exception.JobDesignationNotFoundException;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher publisher;

    @PostMapping(value = {"/addEmployee"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<NewEmployee> addEmployee(@RequestPart("employee") NewEmployee newEmployee,
                                                   @RequestPart("picture")MultipartFile multipartFile) throws UserRoleNotFoundException,
                                                   AppUserNotFoundException, EmployeeNotFoundException {
        Picture picture=new Picture();
        try{
            picture=uploadPicture(multipartFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        newEmployee.setPicture(picture);
        Employee employee=modelMapper.map(newEmployee,Employee.class);
        Employee post=employeeService.addEmployee(employee);
        NewEmployee posted=modelMapper.map(post,NewEmployee.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findEmployeeById")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<EmployeeDto> getEmployeeById(@RequestParam("id") Long id) throws EmployeeNotFoundException {
        Employee employee=employeeService.fetchEmployeeById(id);
        return new ResponseEntity<>(convertEmployeeToDto(employee), OK);
    }

    @GetMapping("/findEmployeeByCode")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<EmployeeDto> getEmployeeByCode(@RequestParam("employeeCode") String employeeCode)
                                                                        throws EmployeeNotFoundException {
        Employee employee=employeeService.fetchEmployeeByCode(employeeCode);
        return new ResponseEntity<>(convertEmployeeToDto(employee),OK);
    }

    @GetMapping("/findEmployeeByUsernameOrEmailOrMobile")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<EmployeeDto> getEmployeeByUserNameOrEmailOrMobile(@RequestParam("searchKey") String searchKey)
                                                                                    throws EmployeeNotFoundException {
        Employee employee= employeeService.fetchEmployeeByUsernameOrMobileOEmail(searchKey);
        return new ResponseEntity<>(convertEmployeeToDto(employee), OK);
    }

    @GetMapping("/findEmployeeByJobDescription")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByJobDescription(@RequestParam("pageNumber") Integer pageNumber,
                              @RequestParam("jobDescription") String jobDescription) throws JobDesignationNotFoundException {
        return new ResponseEntity<>(employeeService.fetchEmployeeByJobDesignation(jobDescription,pageNumber)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()), OK);
    }


    @GetMapping("/findEmployeeByGender")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByGender(@RequestParam("pageNumber") Integer pageNumber,
                                                                 @RequestParam("gender") Gender gender){
        return new ResponseEntity<>(employeeService.fetchEmployeeByGender(pageNumber,gender)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findEmployeeByMaritalStatus")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByMaritalStatus(@RequestParam("pageNumber") Integer pageNumber,
                                                                 @RequestParam("maritalStatus") MaritalStatus maritalStatus){
        return new ResponseEntity<>(employeeService.fetchEmployeeByMaritalStatus(pageNumber,maritalStatus)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findEmploymentByStatus")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<List<EmployeeDto>> getEmployeeByEmploymentStatus(@RequestParam("pageNumber") Integer pageNumber,
                                                      @RequestParam("employmentStatus") EmployeeStatus employeeStatus){
        return new ResponseEntity<>(employeeService.fetchEmployeeByStatus(pageNumber,employeeStatus)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findAllEmployeesOrByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployeesOrByFirstOrLastName(@RequestParam("pageNumber") Integer pageNumber,
                                                                                @RequestParam("searchKey") String searchKey){
        return new ResponseEntity<>(employeeService
                .fetchAllEmployeeOrByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(pageNumber,searchKey)
                .stream()
                .map(this::convertEmployeeToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findAllEmployeesOrByFirstNameOrLastNameOrOtherNames")
    @PreAuthorize("hasAnyRole('HRIS_ADMINISTRATOR','GM_HR', 'SYSTEM_ADMINISTRATOR','SOFTWARE_ENGINEER')")
    public ResponseEntity<List<EmployeeDto>> getEmployeesOrByFirstOrLastName(@RequestParam("pageNumber") Integer pageNumber,
                                                                             @RequestParam("searchKey") String searchKey){
        return new ResponseEntity<>(employeeService
                .fetchAllEmployeeOrByFirstNameOrLastNameOrOtherNamesOrStateOfOrigin(pageNumber,searchKey)
                .stream()
                .map(this::convertEmployeeToDto1)
                .collect(Collectors.toList()), OK);
    }

    @PutMapping("/updateEmployee")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR','GM_HR', 'SYSTEM_ADMINISTRATOR','SOFTWARE_ENGINEER')")
    public ResponseEntity<UpdateEmployee> editEmployee(@RequestBody UpdateEmployee updateEmployee,
                                                       @RequestParam("id") Long id) throws EmployeeNotFoundException {
        Employee employee=modelMapper.map(updateEmployee,Employee.class);
        Employee post=employeeService.updateEmployee(employee,id);
        UpdateEmployee posted=modelMapper.map(post,UpdateEmployee.class);
        return new ResponseEntity<>(posted, OK);
    }

    @DeleteMapping("deleteEmployee")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR','GM_HR', 'SYSTEM_ADMINISTRATOR','SOFTWARE_ENGINEER')")
    public ResponseEntity<?> deleteEmployeeById(@RequestParam("id") Long id) throws EmployeeNotFoundException {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().body("Employee with ID "+id+" Is Deleted");
    }

    @DeleteMapping("deleteAllEmployee")
    @PreAuthorize("hasRole('HRIS_ADMINISTRATOR')")
    public ResponseEntity<?> deleteAllEmployees(){
        employeeService.deleteAllEmployees();
        return ResponseEntity.ok().body("Employees Deleted");
    }

    private Picture uploadPicture(MultipartFile multipartFile) throws IOException {
        Picture picture=new Picture(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getBytes());
        return picture;
    }

    private EmployeeDto convertEmployeeToDto(Employee employee){
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setEmployeeCode(employee.getEmployeeCode());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setOtherNames(employee.getOtherNames());
        employeeDto.setDateOfBirth(employee.getDateOfBirth());
        employeeDto.setAge(employee.getAge());
        employeeDto.setRetirementDate(employee.getRetirementDate());
        employeeDto.setGender(employee.getGender());
        employeeDto.setMaritalStatus(employee.getMaritalStatus());
        employeeDto.setStateOfOrigin(employee.getStateOfOrigin());
        employeeDto.setNationality(employee.getNationality());
        employeeDto.setHiredDate(employee.getHiredDate());
        employeeDto.setEmployeeStatus(employee.getEmployeeStatus());
        employeeDto.setNextOfKinName(employee.getNextOfKinName());
        employeeDto.setRelationshipWithNextOfKin(employee.getRelationshipWithNextOfKin());
        employeeDto.setEmail(employee.getAppUser().getEmail());
        employeeDto.setMobile(employee.getAppUser().getMobile());
        employeeDto.setHouseNumber(employee.getAppUser().getContact().getHouseNumber());
        employeeDto.setStreetName(employee.getAppUser().getContact().getStreetName());
        employeeDto.setCity(employee.getAppUser().getContact().getCity());
        employeeDto.setLandmark(employee.getAppUser().getContact().getLandmark());
        employeeDto.setStateProvince(employee.getAppUser().getContact().getStateProvince());
        employeeDto.setCountry(employee.getAppUser().getContact().getCountry());
        employeeDto.setJobTitle(employee.getJobDesignation().getJobTitle());
        employeeDto.setPicture(employee.getPicture());
        return employeeDto;
    }

    private EmployeeDto convertEmployeeToDto1(Employee employee){
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setOtherNames(employee.getOtherNames());
        employeeDto.setEmail(employee.getAppUser().getEmail());
        employeeDto.setMobile(employee.getAppUser().getMobile());
        employeeDto.setJobTitle(employee.getJobDesignation().getJobTitle());
        employeeDto.setPicture(employee.getPicture());
        return employeeDto;
    }
}
