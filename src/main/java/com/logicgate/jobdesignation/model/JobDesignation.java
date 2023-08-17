package com.logicgate.jobdesignation.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.baseaudit.BaseObject;
import com.logicgate.employee.model.Employee;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class JobDesignation extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobDesignationCode;

    @Column(unique = true)
    private String jobTitle;

    @Column(length = 10000)
    private String jobDescription;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "jobDesignation")
    private Employee employee;
}
