package com.logicgate.event;


import com.logicgate.appuser.model.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private String applicationUrl;
    private AppUser appUser;
    public RegistrationEvent(AppUser appUser,String applicationUrl) {
        super(appUser);
        this.applicationUrl=applicationUrl;
        this.appUser=appUser;
    }
}
