package com.farmmart.data.model.vendor;

import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.staticdata.MeansOfIdentification;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorDto {

    @JsonIgnore
    private Long id;

    private BusinessEntity businessEntity;

    private String name;

    private String rcNumber;

    private String taxId;

    private String natureOfBusiness;

    private MeansOfIdentification meansOfIdentification;

    private String meansOfIdNumber;

    private LocalDate meansOfIdIssueDate;

    private LocalDate meansOfIdExpiryDate;

    private String representative;

    private Status status;

    private String phone;
}
