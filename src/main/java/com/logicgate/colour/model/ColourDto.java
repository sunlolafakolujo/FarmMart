package com.logicgate.colour.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ColourDto {
    private Long id;
    private String colourCode;
    private String colourName;
}
