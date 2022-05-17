package com.farmmart.data.model.customer;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.baseaudit.BaseAudit;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.Status;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerCode;

    @Enumerated(EnumType.STRING)
    private Status customerStatus;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private AppUser appUser;

}
