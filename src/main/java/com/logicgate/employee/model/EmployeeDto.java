package com.logicgate.employee.model;


import com.logicgate.image.model.Picture;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.staticdata.RelationshipWithNextOfKin;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDto {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String otherNames;
    private LocalDate dateOfBirth;
    private LocalDate retirementDate;
    private Integer age;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String stateOfOrigin;
    private String nationality;
    private LocalDate hiredDate;
    private EmployeeStatus employeeStatus;
    private String nextOfKinName;
    private RelationshipWithNextOfKin relationshipWithNextOfKin;
    private String mobile;
    private String email;
    private String houseNumber;
    private String streetName;
    private String city;
    private String landmark;
    private String stateProvince;
    private String country;
    private String jobTitle;
    private Picture picture;
}
