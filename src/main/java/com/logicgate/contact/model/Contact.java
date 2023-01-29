package com.logicgate.contact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.appuser.model.AppUser;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String houseNumber;
    private String streetName;
    private String landmark;
    private String city;
    private String stateProvince;
    private String country;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "contact")
    private AppUser appUser;
}
