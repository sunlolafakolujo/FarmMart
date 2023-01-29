package com.logicgate.event;


import com.logicgate.appuser.model.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class PasswordResetEvent extends ApplicationEvent {
    private String applicationUrl;
    private AppUser appUser;
    public PasswordResetEvent(String applicationUrl,AppUser appUser) {
        super(appUser);
        this.applicationUrl=applicationUrl;
        this.appUser=appUser;
    }
}
