package com.farmmart.data.model.address;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @JsonIgnore
    private Long id;

    private String streetNumber;

    private String streetName;

    private String city;

    private String postZipCode;

    private String localGovernmentName;

    private String stateName;

    private String countryName;
}
