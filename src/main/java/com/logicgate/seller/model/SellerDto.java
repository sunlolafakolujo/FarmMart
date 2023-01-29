package com.logicgate.seller.model;


import com.logicgate.image.model.Picture;
import com.logicgate.staticdata.BusinessEntityType;
import com.logicgate.staticdata.FacilityOwnership;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SellerDto {
    private Long id;
    private String sellerCode;
    private String sellerName;
    private BusinessEntityType businessEntityType;
    private String natureOfBusiness;
    private String companyRepresentative;
    private String taxId;
    private String rcNumber;
    private FacilityOwnership facilityOwnership;
    private String mobile;
    private String email;
    private String streetNumber;
    private String streetName;
    private String city;
    private String landmark;
    private String postZipCode;
    private String stateProvince;
    private String country;
    private List<Picture> taxCacDocuments;
}
