package com.logicgate.appuser.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;

import java.util.List;

public interface AppUserService {
    AppUser addAppUser(AppUser appUser) throws AppUserNotFoundException;
    void addRoleToUser(String searchKey, String roleName) throws UserRoleNotFoundException, AppUserNotFoundException;
    AppUser fetchById(Long id) throws AppUserNotFoundException;
    AppUser fetchByUsernameOrEmailOrMobile(String searchKey) throws AppUserNotFoundException;
    List<AppUser> fetchAllUsers(Integer pageNumber, UserType userType);
    AppUser updateAppUser(AppUser appUser, Long id) throws AppUserNotFoundException;
    void deleteAppUser(Long id) throws AppUserNotFoundException;
    void deleteAllUsers();
}
