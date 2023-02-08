package com.logicgate.farmservices.model;

import com.logicgate.category.model.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FarmServiceDto {
    private Long id;
    private String serviceCode;
    private String serviceName;
    private String serviceDescription;
    private List<Category> categories;
    private String sellerName;
    private String sellerMobile;
    private String houseNumber;
    private String streetName;
    private String city;
    private String landmark;
    private String stateProvince;
    private String country;
}
