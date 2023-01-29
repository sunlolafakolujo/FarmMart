package com.logicgate.employee.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.contact.model.Contact;
import com.logicgate.image.model.Picture;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.staticdata.RelationshipWithNextOfKin;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateEmployee {
    private Long id;
    private String firstName;
    private String lastName;
    private String otherNames;
    private MaritalStatus maritalStatus;
    private EmployeeStatus employeeStatus;
    private String nextOfKinName;
    private RelationshipWithNextOfKin relationshipWithNextOfKin;
    private Picture picture;
    private JobDesignation jobDesignation;
    private AppUser appUser;
}
