package com.farmmart.data.model.employee;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.RelationshipWithNextOfKin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @JsonIgnore
    private Long id;

    private String firstName;

    private String lastName;

    private String otherNames;

    private LocalDate dob;

    private Integer age;

    private Gender gender;

    private String nextOfKin;

    private RelationshipWithNextOfKin relationshipWithNextOfKin;

    private LocalDate hiredDate;

    private String endDate;

    private String phone;

    private String email;

    private List<Address> addresses;
}
