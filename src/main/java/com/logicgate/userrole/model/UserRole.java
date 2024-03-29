package com.logicgate.userrole.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.baseaudit.BaseObject;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class UserRole extends BaseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String roleName;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "userRoles", cascade = CascadeType.ALL)
    private List<AppUser> appUsers;
}
