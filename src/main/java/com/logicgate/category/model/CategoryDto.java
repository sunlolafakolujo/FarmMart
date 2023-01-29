package com.logicgate.category.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {
    private Long id;
    private String categoryCode;
    private String categoryName;
}
