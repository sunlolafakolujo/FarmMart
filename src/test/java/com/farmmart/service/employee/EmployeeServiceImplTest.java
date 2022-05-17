package com.farmmart.service.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.employee.EmployeeNotFoundException;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.employee.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private EmployeeService employeeService=new EmployeeServiceImpl();

    Employee employee;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employee=new Employee();
        appUser=new AppUser();
    }

    @Test
    void testThatYouCanMockSaveEmployeeMethod() throws EmployeeNotFoundException {
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        employeeService.saveEmployee(employee);

        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);

        Mockito.verify(employeeRepository,Mockito.times(1)).save(employeeArgumentCaptor.capture());

        Employee captureEmployee=employeeArgumentCaptor.getValue();

        assertEquals(captureEmployee,employee);
    }

    @Test
    void testThatYouCanMockFindEmployeeByIdMethod() throws EmployeeNotFoundException {
        Long id=1L;
        Mockito.when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        employeeService.findEmployeeById(id);

        Mockito.verify(employeeRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindEmployeeByFirstNameMethod() throws EmployeeNotFoundException {
        List<Employee> employees=new ArrayList<>();

        String firstName="Pamilerin";
        Mockito.when(employeeRepository.findByFirstName(firstName)).thenReturn(employees);

        employeeService.findEmployeeByFirstName(firstName);

        Mockito.verify(employeeRepository,Mockito.times(1)).findByFirstName(firstName);
    }

    @Test
    void testThatYouCanMockFindEmployeeByLastNameMethod() throws EmployeeNotFoundException {
        List<Employee> employees=new ArrayList<>();

        String lastName="akin";
        Mockito.when(employeeRepository.findByLastName(lastName)).thenReturn(employees);

        employeeService.findEmployeeByLastName(lastName);

        Mockito.verify(employeeRepository,Mockito.times(1)).findByLastName(lastName);

    }

    @Test
    void testThatYouCanMockFindEmployeeByGenderMethod() throws EmployeeNotFoundException {

        Gender gender=Gender.MALE;

        List<Employee> employees=new ArrayList<>();
        Mockito.when(employeeRepository.findByGender(gender)).thenReturn(employees);

        employeeService.findEmployeeByGender(gender);

        Mockito.verify(employeeRepository,Mockito.times(1)).findByGender(gender);
    }

    @Test
    void testThatYouCanMockFindEmployeeByUsername() {
        String username="HephzibahPam";
        appUser=appUserRepository.findByUserName(username);

        Mockito.when(employeeRepository.findByUsername(appUser)).thenReturn(employee);

        employeeService.findEmployeeByUsername(appUser,username);

        Mockito.verify(employeeRepository,Mockito.times(1)).findByUsername(appUser);
    }

    @Test
    void testThatYouCanMockFindAllEmployeeMethod() {
        List<Employee> employees=new ArrayList<>();

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        employeeService.findAllEmployee(10);

        Mockito.verify(employeeRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockUpdateEmployeeDetailsMethod() throws EmployeeNotFoundException {

        Long id=1L;
        Mockito.when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        employeeService.updateEmployeeDetails(employee,id);

        Mockito.verify(employeeRepository,Mockito.times(1)).save(employee);
    }

    @Test
    void testThaYouCanMockDeleteEmployeeByIdMethod() throws EmployeeNotFoundException {
        Long id=2L;
        doNothing().when(employeeRepository).deleteById(id);

        employeeService.deleteEmployeeById(id);

        verify(employeeRepository,times(1)).deleteById(id);
    }

    @Test
    void testThaYouCanMockDeleteAllEmployeeMethod() {

        doNothing().when(employeeRepository).deleteAll();

        employeeService.deleteAllEmployee();

        verify(employeeRepository,times(1)).deleteAll();

    }
}