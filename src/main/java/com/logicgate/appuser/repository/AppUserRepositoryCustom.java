package com.logicgate.appuser.repository;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.staticdata.UserType;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserRepositoryCustom {
    @Query("FROM AppUser a WHERE a.username=?1 OR a.email=?2 OR a.mobile=?3")
    Optional<AppUser> findUserByUsernameOrEmailOrMobileIgnoreCase(String username, String email, String mobile);

    @Query("FROM AppUser a WHERE a.userType=?1")
    List<AppUser> findByUserType(UserType userType);
}
