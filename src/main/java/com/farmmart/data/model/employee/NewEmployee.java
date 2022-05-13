package com.farmmart.data.model.employee;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.RelationshipWithNextOfKin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEmployee {

    private String firstName;

    private String lastName;

    private String otherNames;

    private LocalDate dob;

    private Integer age;

    private Gender gender;

    private String nextOfKin;

    private RelationshipWithNextOfKin relationshipWithNextOfKin;

    private LocalDate hiredDate;

    private AppUser appUser;
}
