package com.logicgate.jobdesignation.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateJobDesignation {
    private Long id;
    private String jobDesignationCode;
    private String jobTitle;
    private String jobDescription;
}
