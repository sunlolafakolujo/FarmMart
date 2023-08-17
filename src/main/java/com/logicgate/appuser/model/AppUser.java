package com.logicgate.appuser.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.verificationtoken.model.VerificationToken;
import com.logicgate.baseaudit.BaseObject;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.contact.model.Contact;
import com.logicgate.employee.model.Employee;
import com.logicgate.passwordtoken.model.PasswordVerificationToken;
import com.logicgate.seller.model.Seller;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.model.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AppUser extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userCode;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(unique = true)
    private String username;
    private String password;

    @Transient
    private String confirmPassword;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private Boolean isEnabled=false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Contact contact;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "app_user_roles",
    joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_role_id", referencedColumnName = "id"))
    private List<UserRole> userRoles=new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser")
    private VerificationToken verificationToken;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser")
    private Employee employee;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser")
    private PasswordVerificationToken passwordVerificationToken;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser")
    private Seller seller;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser")
    private Buyer buyer;
}
