package com.logicgate.image.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PictureDto {
    private Long id;
    private String name;
    private String imageType;
    private byte[] picByte;
}
