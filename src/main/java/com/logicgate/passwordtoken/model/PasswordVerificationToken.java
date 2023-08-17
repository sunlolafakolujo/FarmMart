package com.logicgate.passwordtoken.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.baseaudit.BaseObject;
import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class PasswordVerificationToken extends BaseObject {
    private static final Integer EXPIRATION_TIME=20;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expectedExpirationTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppUser appUser;

    public PasswordVerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expectedExpirationTime=calculateExpirationTime(EXPIRATION_TIME);
    }

    public PasswordVerificationToken(String token) {
        this.token = token;
        this.expectedExpirationTime=calculateExpirationTime(EXPIRATION_TIME);
    }

    private Date calculateExpirationTime(Integer expirationTime) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
