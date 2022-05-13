package com.farmmart.data.model.appuser;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.baseaudit.BaseAudit;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppUser extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Email(message = "Email is already taken")
    private String email;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "appUser",fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.DETACH,
            CascadeType.PERSIST,CascadeType.REFRESH})
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    private Collection<UserRole> userRoles;

}
