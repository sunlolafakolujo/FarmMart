package com.logicgate.product.model;


import com.logicgate.category.model.Category;
import com.logicgate.image.model.Picture;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateProduct {
    private Long id;
    private BigDecimal price;
    private Integer availableStockQuantity;
    private String productSKU;
    private List<Picture> pictures=new ArrayList<>();
}
