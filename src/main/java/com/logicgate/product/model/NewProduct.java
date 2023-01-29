package com.logicgate.product.model;


import com.logicgate.category.model.Category;
import com.logicgate.colour.model.Colour;
import com.logicgate.image.model.Picture;
import com.logicgate.seller.model.Seller;
import com.logicgate.staticdata.ProductAvailability;
import com.logicgate.staticdata.ProductCondition;
import com.logicgate.staticdata.UnitOfMeasure;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewProduct {
    private String productType;
    private String productCode;
    private String productName;
    private String productDescription;
    private String manufacturerWebSite;
    private BigDecimal price;
//    private BigDecimal discountedPrice;
    private Integer availableStockQuantity;
    private String brand;
    private String specification;
    private String productSKU;
    private ProductCondition productCondition;
    private UnitOfMeasure unitOfMeasure;
    private ProductAvailability productAvailability;
    private List<Category> categories;
    private List<Colour> colours=new ArrayList<>();
    private List<Picture> pictures=new ArrayList<>();
    private Seller seller;
}
