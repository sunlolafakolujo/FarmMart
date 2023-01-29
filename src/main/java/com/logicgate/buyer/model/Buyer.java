package com.logicgate.buyer.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.baseaudit.BaseAudit;
import com.logicgate.staticdata.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Buyer extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String buyerCode;
    private String firstName;
    private String otherName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppUser appUser;

    public Buyer(String buyerCode,String firstName, String lastName){
        this.buyerCode=buyerCode;
        this.firstName=firstName;
        this.lastName=lastName;
    }
}
