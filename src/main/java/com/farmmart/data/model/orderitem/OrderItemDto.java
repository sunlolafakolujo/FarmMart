package com.farmmart.data.model.orderitem;

import com.farmmart.data.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Product product;

    private Integer orderQuantity;
}
