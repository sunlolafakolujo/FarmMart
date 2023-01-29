package com.logicgate.buyer.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.staticdata.Gender;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewBuyer {
    private String buyerCode;
    private String firstName;
    private String otherName;
    private String lastName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private AppUser appUser;
}
