package com.farmmart.controller.appuser;

import com.farmmart.data.model.appuser.*;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.service.appuser.AppUserDetailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appUser")
@RequiredArgsConstructor
@Slf4j
public class AppUserRestController {

    private final AppUserDetailService appUserService;

    private final ModelMapper modelMapper;

    @PostMapping("/userRegistration")
    public ResponseEntity<NewAppUser> saveAppUser(@Valid @RequestBody NewAppUser newAppUser)
                                                        throws AppUserNotFoundException {

        AppUser appUser=modelMapper.map(newAppUser,AppUser.class);

        AppUser postAppUser=appUserService.saveAppUser(appUser);

        NewAppUser postedAppUser=modelMapper.map(postAppUser,NewAppUser.class);

        return new ResponseEntity<>(postedAppUser, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_VENDOR','ROLE_EMPLOYEE','ROLE_ADMIN')")
    public ResponseEntity<AppUserLogIn> signIn(@RequestBody AppUserLogIn appUserSignInDto) throws AppUserNotFoundException {
        AppUser appUser=modelMapper.map(appUserSignInDto, AppUser.class);

        AppUser post=appUserService.userLogIn(appUser);

        AppUserLogIn posted=modelMapper.map(post, AppUserLogIn.class);

        return ResponseEntity.ok().body(posted);
    }

    @PostMapping("/addRoleToUser/{username}, {roleName}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addRoleToUser(@PathVariable(value = "username")String username,@PathVariable(value = "roleName") String roleName){

        appUserService.addRoleToUser(username,roleName);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/findUserById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable(value = "id") Long id) throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserById(id);

        AppUserDto appUserDto=convertAppUserToDto(appUser);

        return ResponseEntity.ok().body(appUserDto);
    }

    @GetMapping("/findUserByUsername/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserDto> getUserByUsername(@PathVariable(value = "username") String username) throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserByUsername(username);

        AppUserDto appUserDto=convertAppUserToDto(appUser);

        return ResponseEntity.ok().body(appUserDto);
    }

    @GetMapping("/findUserByEmail/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserDto> getUserByEmail(@PathVariable(value = "email") String email) throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserByEmail(email);

        AppUserDto appUserDto=convertAppUserToDto(appUser);

        return ResponseEntity.ok().body(appUserDto);
    }

    @GetMapping("/findUserByPhoneNumber/{phone}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserDto> getUserByPhoneNumber(@PathVariable(value = "phone") String phone) throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserByPhoneNumber(phone);

        AppUserDto appUserDto=convertAppUserToDto(appUser);

        return ResponseEntity.ok().body(appUserDto);
    }

    @GetMapping("/findUserByType/{type}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUserDto>> getUserByType(@PathVariable(value = "type") UserType userType){

        return ResponseEntity.ok().body(appUserService.findUserByType(userType)
                .stream()
                .map(this::convertAppUserToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findAllUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUserDto>> getAllusers(){

        return ResponseEntity.ok().body(appUserService.findAllUsers()
                .stream()
                .map(this::convertAppUserToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateUserById/{id}")
    public ResponseEntity<ModifyAppUser> updateUser(@Valid @RequestBody ModifyAppUser modifyAppUser,
                                                    @PathVariable(value = "id") Long id) throws AppUserNotFoundException {

        AppUser appUser=modelMapper.map(modifyAppUser,AppUser.class);

        AppUser updateAppUser=appUserService.updateUser(appUser,id);

        ModifyAppUser updatedAppUser=modelMapper.map(updateAppUser,ModifyAppUser.class);

        return ResponseEntity.ok().body(updatedAppUser);
    }

    @DeleteMapping("/deleteUserById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserByid(@PathVariable(value = "id") Long id) throws AppUserNotFoundException {

        appUserService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllUsers(){

        appUserService.deleteAllUsers();

        return ResponseEntity.noContent().build();
    }

    private AppUserDto convertAppUserToDto(AppUser appUser){

        AppUserDto appUserDto=new AppUserDto();
        appUserDto.setId(appUser.getId());
        appUserDto.setUsername(appUser.getUsername());
        appUserDto.setPassword(appUser.getPassword());
        appUserDto.setPhone(appUser.getPhone());
        appUserDto.setEmail(appUser.getEmail());
        appUserDto.setUserType(appUser.getUserType());
        appUserDto.setUserRoles(appUser.getUserRoles());
        appUserDto.setAddresses(appUser.getAddresses());

        return appUserDto;
    }

    @Data
    class RoleToUser{
        String username;

        String role;
    }
}





