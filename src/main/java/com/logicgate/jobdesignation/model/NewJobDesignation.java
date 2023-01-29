package com.logicgate.jobdesignation.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewJobDesignation {
    private String jobDesignationCode;
    private String jobTitle;
    private String jobDescription;
}
