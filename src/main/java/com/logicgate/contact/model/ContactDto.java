package com.logicgate.contact.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ContactDto {
    private Long id;
    private String houseNumber;
    private String streetName;
    private String landmark;
    private String city;
    private String stateProvince;
    private String country;
}
