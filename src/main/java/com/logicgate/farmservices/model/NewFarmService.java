package com.logicgate.farmservices.model;

import com.logicgate.category.model.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewFarmService {
    private Long id;
    private String serviceCode;
    private String serviceName;
    private String serviceDescription;
    private List<Category> categories=new ArrayList<>();
}
