package com.logicgate.farmservices.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateFarmService {
    private Long id;
    private String serviceDescription;
}
