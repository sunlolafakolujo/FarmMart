package com.logicgate.appuser.model;


import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.model.UserRole;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AppUserDto {
    private String userCode;
    private UserType userType;
    private String email;
    private String mobile;
    private String streetNumber;
    private String streetName;
    private String city;
    private String landmark;
    private String postZipCode;
    private String stateProvince;
    private String country;
    private Collection<UserRole> userRoles=new HashSet<>();
}
