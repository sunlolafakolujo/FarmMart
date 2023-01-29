package com.logicgate.image.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.employee.model.Employee;
import com.logicgate.product.model.Product;
import com.logicgate.seller.model.Seller;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Picture {
    public Picture(String name, String imageType, byte[] picByte) {
        this.name = name;
        this.imageType = imageType;
        this.picByte = picByte;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String imageType;

    @Column(length = 10000000)
    private byte[] picByte;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "picture")
    private Employee employee;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "taxCacDocuments")
    private List<Seller> sellers;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "pictures")
    private List<Product> products;
}
