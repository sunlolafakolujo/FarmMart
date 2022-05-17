package com.farmmart.data.model.vendor;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.baseaudit.BaseAudit;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.services.Service;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.staticdata.Facility;
import com.farmmart.data.model.staticdata.MeansOfIdentification;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vendor extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendorCode;

    @Enumerated(EnumType.STRING)
    private BusinessEntity businessEntity;

//    @Column(unique = true)
    private String name;

//    @Column(unique = true)
    private String rcNumber;

//    @Column(unique = true)
    private String taxId;

    /**
     * Text Area
     */
//    @NotNull
    @Size(max = 10000)
    private String natureOfBusiness;

    @Enumerated(EnumType.STRING)
    private Status vendorStatus;

    @Enumerated(EnumType.STRING)
    private MeansOfIdentification meansOfIdentification;

    private String meansOfIdNumber;

    @JsonFormat
    private LocalDate meansOfIdIssueDate;

    @JsonFormat
    private LocalDate meansOfIdExpiryDate;

    private String representative;

    @Enumerated(EnumType.STRING)
    private Facility facility;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private AppUser appUser;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "vendor",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Product> products;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Service> services;

    public void addProduct(Product product){
        if (product==null){
            products=new ArrayList<>();
        }
        products.add(product);

        product.setVendor(this);
    }

    public void addService(Service service){
        if (services==null){
            services=new ArrayList<>();
        }
        services.add(service);

        service.setVendor(this);
    }

}
