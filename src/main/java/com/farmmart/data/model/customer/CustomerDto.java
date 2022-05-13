package com.farmmart.data.model.customer;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonIgnore
    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private AgeRange ageRange;

    private String phone;

    private List<Address> addresses;
}
