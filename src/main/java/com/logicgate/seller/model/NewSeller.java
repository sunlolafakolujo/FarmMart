package com.logicgate.seller.model;


import com.logicgate.appuser.model.AppUser;
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
public class NewSeller {
    private String sellerCode;
    private String sellerName;
    private BusinessEntityType businessEntityType;
    private String natureOfBusiness;
    private String companyRepresentative;
    private String taxId;
    private String rcNumber;
    private FacilityOwnership facilityOwnership;
    private AppUser appUser;
    private List<Picture> taxCacDocuments;
}
