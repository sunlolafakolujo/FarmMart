package com.logicgate.userrole.controller;


import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.NewUserRole;
import com.logicgate.userrole.model.UpdateUserRole;
import com.logicgate.userrole.model.UserRole;
import com.logicgate.userrole.model.UserRoleDto;
import com.logicgate.userrole.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserRoleController {
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;

    @PostMapping("/addRole")
    public ResponseEntity<NewUserRole> addRole(@RequestBody NewUserRole newUserRole) throws UserRoleNotFoundException {
        UserRole userRole=modelMapper.map(newUserRole,UserRole.class);
        UserRole post=userRoleService.addRole(userRole);
        NewUserRole posted=modelMapper.map(post,NewUserRole.class);
        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @GetMapping("/findRoleById")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<UserRoleDto> getRoleById(@RequestParam("id") Long id) throws UserRoleNotFoundException {
        UserRole userRole=userRoleService.fetchRoleById(id);
        return new ResponseEntity<>(convertUserRoleToDto(userRole), HttpStatus.OK);
    }

    @GetMapping("/findRoleByName")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<UserRoleDto> getRoleByName(@RequestParam("roleName") String roleName) throws UserRoleNotFoundException {
        UserRole userRole=userRoleService.fetchRoleByName(roleName);
        return new ResponseEntity<>(convertUserRoleToDto(userRole),HttpStatus.OK);
    }

    @GetMapping("/findAllRoles")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<List<UserRoleDto>> getAllRoles(@RequestParam(defaultValue = "0") Integer pageNumber){
        return new ResponseEntity<>(userRoleService.fetchAllRoles(pageNumber)
                .stream()
                .map(this::convertUserRoleToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/updateRole")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<UpdateUserRole> editRole(@RequestBody UpdateUserRole updateUserRole,
                                                   @RequestParam("id") Long id) throws UserRoleNotFoundException {
        UserRole userRole=modelMapper.map(updateUserRole,UserRole.class);
        UserRole post=userRoleService.updateUserRole(userRole,id);
        UpdateUserRole posted=modelMapper.map(post,UpdateUserRole.class);
        return new ResponseEntity<>(posted,HttpStatus.OK);
    }

    @DeleteMapping("/deleteRole")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<?> deleteRoleById(@RequestParam("id") Long id) throws UserRoleNotFoundException {
        userRoleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllRoles")
    @PreAuthorize("hasRole('SYSTEM_ADMINISTRATOR')")
    public ResponseEntity<?> deleteAllRoles(){
        userRoleService.deleteAllRoles();
        return ResponseEntity.noContent().build();
    }


    private UserRoleDto convertUserRoleToDto(UserRole userRole){
        UserRoleDto userRoleDto=new UserRoleDto();
        userRoleDto.setId(userRole.getId());
        userRoleDto.setRoleName(userRole.getRoleName());
        return userRoleDto;
    }
}
