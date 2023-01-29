package com.logicgate.colour.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.baseaudit.BaseAudit;
import com.logicgate.product.model.Product;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Colour extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String colourCode;

    @Column(unique = true)
    private String colourName;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "colours")
    private List<Product> products;
}
