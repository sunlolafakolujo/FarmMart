package com.farmmart.data.model.cart;

import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    @JsonIgnore
    private Long id;

    private Integer orderQuantity;

    private Product product;

    public CartItemDto(Cart cart) {
        this.setId(cart.getId());
        this.setOrderQuantity(cart.getOrderQuantity());
        this.setProduct(cart.getProduct());

    }
}
