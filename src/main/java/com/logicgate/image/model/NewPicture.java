package com.logicgate.image.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewPicture {
    private String name;
    private String imageType;
    private byte[] picByte;
}
