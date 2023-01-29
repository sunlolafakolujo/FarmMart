package com.logicgate.userrole.service;


import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.UserRole;
import com.logicgate.userrole.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService{
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole addRole(UserRole userRole) throws UserRoleNotFoundException {
        Optional<UserRole> saveRole=userRoleRepository.findByUserRoleName(userRole.getRoleName());
        if (saveRole.isPresent()){
            throw new UserRoleNotFoundException("Role already exist");
        }
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole fetchRoleById(Long id) throws UserRoleNotFoundException {
        return userRoleRepository.findById(id).orElseThrow(()->new UserRoleNotFoundException("Role Not Found"));
    }

    @Override
    public UserRole fetchRoleByName(String roleName) throws UserRoleNotFoundException {
        return userRoleRepository.findByUserRoleName(roleName)
                .orElseThrow(()->new UserRoleNotFoundException("Role Not Found"));
    }

    @Override
    public List<UserRole> fetchAllRoles(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return userRoleRepository.findAll(pageable).toList();
    }

    @Override
    public UserRole updateUserRole(UserRole userRole, Long id) throws UserRoleNotFoundException {
        UserRole savedRole=userRoleRepository.findById(id)
                .orElseThrow(()->new UserRoleNotFoundException("Role Not Found"));
        if (Objects.nonNull(userRole.getRoleName()) && !"".equalsIgnoreCase(userRole.getRoleName())){
            savedRole.setRoleName(userRole.getRoleName());
        }
        return userRoleRepository.save(savedRole);
    }

    @Override
    public void deleteRoleById(Long id) throws UserRoleNotFoundException {
        if (userRoleRepository.existsById(id)){
            userRoleRepository.deleteById(id);
        }else {
            throw new UserRoleNotFoundException("Role with ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllRoles() {
        userRoleRepository.deleteAll();
    }
}
