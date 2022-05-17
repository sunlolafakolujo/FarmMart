package com.farmmart.data.model.wishlist;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Product product;
}
