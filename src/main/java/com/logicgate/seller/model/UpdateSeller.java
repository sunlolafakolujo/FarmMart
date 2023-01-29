package com.logicgate.seller.model;


import com.logicgate.staticdata.FacilityOwnership;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateSeller {
    private Long id;
    private String companyRepresentative;
    private FacilityOwnership facilityOwnership;
}
