package com.logicgate.seller.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.baseaudit.BaseAudit;
import com.logicgate.image.model.Picture;
import com.logicgate.product.model.Product;
import com.logicgate.staticdata.BusinessEntityType;
import com.logicgate.staticdata.FacilityOwnership;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Seller extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sellerCode;
    private String sellerName;

    @Enumerated(EnumType.STRING)
    private BusinessEntityType businessEntityType;
    private String natureOfBusiness;

    @Column(unique = true)
    private String companyRepresentative;

    @Column(unique = true)
    private String taxId;

    @Column(unique = true)
    private String rcNumber;

    @Enumerated(EnumType.STRING)
    private FacilityOwnership facilityOwnership;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @JoinTable(name = "seller_pictures",
    joinColumns = @JoinColumn(name = "seller_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "picture_id", referencedColumnName = "id"))
    private List<Picture> taxCacDocuments;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "seller")
    private List<Product> products;
}
