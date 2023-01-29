package com.logicgate.employee.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.image.model.Picture;
import com.logicgate.jobdesignation.model.JobDesignation;
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
public class NewEmployee {
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String otherNames;
    private LocalDate dateOfBirth;
    private Integer age;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String stateOfOrigin;
    private String nationality;
    private LocalDate hiredDate;
    private EmployeeStatus employeeStatus;
    private String nextOfKinName;
    private RelationshipWithNextOfKin relationshipWithNextOfKin;
    private Picture picture;
    private AppUser appUser;
    private JobDesignation jobDesignation;
}
