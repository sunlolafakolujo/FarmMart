package com.logicgate.farmservices.model;

import com.logicgate.category.model.Category;
import com.logicgate.seller.model.Seller;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "services")
public class FarmService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceCode;
    private String serviceName;

    @Column(length = 50000)
    private String serviceDescription;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "farm_service_categories",
    joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categories=new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private Seller seller;

    public FarmService(String serviceName, String serviceDescription, List<Category> categories, Seller seller) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.categories = categories;
        this.seller=seller;
    }
}
