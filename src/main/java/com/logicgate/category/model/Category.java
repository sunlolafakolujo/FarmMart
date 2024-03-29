package com.logicgate.category.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.baseaudit.BaseObject;
import com.logicgate.farmservices.model.FarmService;
import com.logicgate.product.model.Product;
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
public class Category extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryCode;

    @Column(unique = true)
    private String categoryName;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "categories")
    private List<Product> products;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "categories")
    private List<FarmService> farmServices=new ArrayList<>();
}
