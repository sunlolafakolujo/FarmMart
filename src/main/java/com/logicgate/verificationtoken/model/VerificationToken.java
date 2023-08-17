package com.logicgate.verificationtoken.model;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.baseaudit.BaseObject;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class VerificationToken extends BaseObject {
    private static final Integer EXPIRATION_TIME=30;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expectedExpirationTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AppUser appUser;

    public VerificationToken(String token, AppUser appUser) {
        this.token = token;
        this.appUser = appUser;
        this.expectedExpirationTime=calculateExpirationTime(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
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
