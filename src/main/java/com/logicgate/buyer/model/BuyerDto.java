package com.logicgate.buyer.model;


import com.logicgate.staticdata.Gender;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BuyerDto {
    private Long id;
    private String buyerCode;
    private String firstName;
    private String otherName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Integer age;
    private String email;
    private String mobile;
    private String houseNumber;
    private String streetName;
    private String city;
    private String landmark;
    private String stateProvince;
    private String country;
}
