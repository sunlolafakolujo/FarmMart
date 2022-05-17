package com.farmmart.data.model.wishlist;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListDto {

    @JsonIgnore
    private Long id;

    private LocalDate createdDate;

    private AppUser appUser;

    private Product product;
}
