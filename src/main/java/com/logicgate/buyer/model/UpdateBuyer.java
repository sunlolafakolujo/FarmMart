package com.logicgate.buyer.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateBuyer {
    private String firstName;
    private String otherName;
    private String lastName;
    private String mobile;
    private String email;
}
