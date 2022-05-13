package com.farmmart.controller.userrolerestcontrol;

import com.farmmart.data.model.userrole.*;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userRole")
@RequiredArgsConstructor
public class UserRoleRestController {

    private final UserRoleServiceImpl userRoleService;

    private final ModelMapper modelMapper;

    @PostMapping("/saveRole")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewUserRole> saveNewRole(@Valid @RequestBody NewUserRole newUserRole) throws UserRoleNotFoundException {
        UserRole userRole=modelMapper.map(newUserRole,UserRole.class);

        UserRole saveRole=userRoleService.saveUserRole(userRole);

        NewUserRole postSavedRole=modelMapper.map(saveRole,NewUserRole.class);

        return new ResponseEntity<>(postSavedRole, HttpStatus.CREATED);
    }

    @GetMapping("/findRoleById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserRoleDto> getRoleById(@PathVariable(value = "id") Long id) throws UserRoleNotFoundException {
        UserRole roleId=userRoleService.findUserRoleById(id);

        UserRoleDto userRoleDto=convertUserRoleToDto(roleId);

        return ResponseEntity.ok().body(userRoleDto);
    }

    @GetMapping("/findRoleByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserRoleDto> getRoleByName(@PathVariable(value = "name") String name) throws UserRoleNotFoundException {
        UserRole roleName=userRoleService.findUserRoleByName(name);

        UserRoleDto userRoleDto=convertUserRoleToDto(roleName);

        return ResponseEntity.ok().body(userRoleDto);
    }

    @GetMapping("/findAllRoles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserRoleDto>> getAllRoles(){

        return ResponseEntity.ok().body(userRoleService.findAllUserRoles()
                .stream()
                .map(this::convertUserRoleToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateRoleById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyUserRole> updateRole(@Valid @RequestBody ModifyUserRole modifyUserRole,
                                                     @PathVariable(value = "id") Long id) throws UserRoleNotFoundException {

        UserRole userRole=modelMapper.map(modifyUserRole,UserRole.class);

        UserRole updateRole=userRoleService.updateUserRole(userRole,id);

        ModifyUserRole updatedRole=modelMapper.map(updateRole,ModifyUserRole.class);

        return ResponseEntity.ok().body(updatedRole);
    }

    @DeleteMapping("/deleteRoleById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteRoleById(@PathVariable(value = "id") Long id) throws UserRoleNotFoundException {
        userRoleService.deleteUserRoleById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllRoles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllRoles(){
        userRoleService.deleteAllUserRoles();

        return ResponseEntity.noContent().build();
    }


    private UserRoleDto convertUserRoleToDto(UserRole userRole){
        UserRoleDto userRoleDto=new UserRoleDto();

        userRoleDto.setId(userRole.getId());
        userRoleDto.setRoleName(userRole.getRoleName());

        return userRoleDto;
    }
}
