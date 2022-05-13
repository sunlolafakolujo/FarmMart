package com.farmmart.data.model.colour;

import com.farmmart.data.model.baseaudit.BaseAudit;
import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Colour extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String colourName;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(mappedBy = "colours",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Collection<Product> products;
}
