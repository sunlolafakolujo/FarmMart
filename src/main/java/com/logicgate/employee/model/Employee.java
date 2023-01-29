package com.logicgate.employee.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.image.model.Picture;
import com.logicgate.jobdesignation.model.JobDesignation;
import com.logicgate.staticdata.EmployeeStatus;
import com.logicgate.staticdata.Gender;
import com.logicgate.staticdata.MaritalStatus;
import com.logicgate.staticdata.RelationshipWithNextOfKin;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeCode;
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    private String otherNames;

    @Column(nullable = false)
    private LocalDate dateOfBirth;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;
    private String stateOfOrigin;
    private String nationality;
    private LocalDate hiredDate;
    private String nextOfKinName;

    @Enumerated(EnumType.STRING)
    private RelationshipWithNextOfKin relationshipWithNextOfKin;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Picture picture;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private AppUser appUser;

    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private JobDesignation jobDesignation;
}
