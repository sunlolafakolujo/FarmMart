package com.logicgate.appuser.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.model.AppUserDto;
import com.logicgate.appuser.model.NewAppUser;
import com.logicgate.appuser.model.UpdateAppUser;
import com.logicgate.appuser.service.AppUserService;
import com.logicgate.event.RegistrationEvent;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.AddRoleToUser;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher publisher;

    @PostMapping("/addUser")
    public ResponseEntity<NewAppUser> addUser(@RequestBody NewAppUser newAppUser, HttpServletRequest request)
                                                                                throws AppUserNotFoundException {
        AppUser appUser=modelMapper.map(newAppUser, AppUser.class);
        AppUser post=appUserService.addAppUser(appUser);
        publisher.publishEvent(new RegistrationEvent(post,applicationUrl(request)));
        NewAppUser posted=modelMapper.map(post,NewAppUser.class);
        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @PostMapping("/addRoleToUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody AddRoleToUser addRoleToUser)
                                                     throws AppUserNotFoundException, UserRoleNotFoundException {
        appUserService.addRoleToUser(addRoleToUser.getUsernameOrEmailOrPassword(),addRoleToUser.getRoleName());
        return ResponseEntity.ok().body("Role Successfully added to user");
    }

    @GetMapping("/findUserById")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUserDto> getUserById(@RequestParam("id") Long id) throws AppUserNotFoundException {
        AppUser appUser=appUserService.fetchById(id);
        return new ResponseEntity<>(convertAppUserToDto(appUser),HttpStatus.OK);
    }

    @GetMapping("/findUserByUsernameOrEmailOrMobile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUserDto> getUserByUsernameOrEmailOrMobile(@RequestParam("searchKey") String searchKey)
                                                                                throws AppUserNotFoundException {
        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(searchKey);
        return new ResponseEntity<>(convertAppUserToDto(appUser),HttpStatus.OK);
    }

    @GetMapping("/findAllUserOrByUserType")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUserDto>> getAllUserOrByUserType(@RequestParam("pageNumber") Integer pageNumber,
                                                                   @RequestParam("userType") UserType userType){
        return  new ResponseEntity<>(appUserService.fetchAllUsers(pageNumber,userType)
                .stream()
                .map(this::convertAppUserToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateAppUser> editAppUser(@RequestBody UpdateAppUser updateAppUser, @RequestParam("id") Long id) throws AppUserNotFoundException {
        AppUser appUser=modelMapper.map(updateAppUser,AppUser.class);
        AppUser post=appUserService.updateAppUser(appUser,id);
        UpdateAppUser posted=modelMapper.map(post,UpdateAppUser.class);
        return new ResponseEntity<>(posted,HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserById(@RequestParam("id") long id) throws AppUserNotFoundException {
        appUserService.deleteAppUser(id);
        return ResponseEntity.ok().body("User successfully deleted");
    }

    @DeleteMapping("/deleteAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllUsers(){
        appUserService.deleteAllUsers();
        return ResponseEntity.ok().body("Users are successfully deleted");
    }

    private AppUserDto convertAppUserToDto(AppUser appUser){
        AppUserDto appUserDto=new AppUserDto();
        appUserDto.setUserCode(appUser.getUserCode());
        appUserDto.setUserType(appUser.getUserType());
        appUserDto.setEmail(appUser.getEmail());
        appUserDto.setMobile(appUser.getMobile());
        appUserDto.setStreetNumber(appUser.getContact().getHouseNumber());
        appUserDto.setStreetName(appUser.getContact().getStreetName());
        appUserDto.setCity(appUser.getContact().getCity());
        appUserDto.setLandmark(appUser.getContact().getLandmark());
        appUserDto.setStateProvince(appUser.getContact().getStateProvince());
        appUserDto.setCountry(appUser.getContact().getCountry());
        appUserDto.setUserRoles(appUser.getUserRoles());
        return appUserDto;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+ ":"+request.getServerPort()+request.getContextPath();
    }
}
