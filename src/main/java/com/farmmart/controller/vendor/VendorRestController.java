package com.farmmart.controller.vendor;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.userrole.UserRoleNotFoundException;
import com.farmmart.data.model.vendor.*;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import com.farmmart.service.vendor.VendorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/vendor")
@RequiredArgsConstructor
public class VendorRestController {

    private final VendorServiceImpl vendorService;

    private final AppUserDetailService appUserService;

    private final ModelMapper modelMapper;

    private final UserRoleServiceImpl userRoleService;

    @PostMapping("/vendorRegistration")
    public ResponseEntity<NewVendor> registerVendor(@Valid @RequestBody NewVendor newVendor) throws UserRoleNotFoundException, VendorNotFoundException {

        newVendor.getAppUser().setUserType(UserType.VENDOR);

        UserRole role=userRoleService.findUserRoleByName("ROLE_VENDOR");

        List<UserRole> roles=new ArrayList<>();
        roles.add(role);

        newVendor.getAppUser().setUserRoles(roles);

        Vendor vendor=modelMapper.map(newVendor,Vendor.class);

        Vendor saveVendor=vendorService.saveVendor(vendor);

        NewVendor posted=modelMapper.map(saveVendor,NewVendor.class);

        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @GetMapping("/findVendorById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<VendorDto> getVendorById(@PathVariable(value = "id") Long id) throws VendorNotFoundException {

        Vendor vendor=vendorService.findVendorById(id);

        VendorDto vendorDto=convertVendorToDto(vendor);

        return ResponseEntity.ok().body(vendorDto);
    }

    @GetMapping("/findVendorByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<VendorDto> getVendorByName(@PathVariable(value = "name") String name) throws VendorNotFoundException {

        Vendor vendor=vendorService.findVendorByName(name);

        VendorDto vendorDto=convertVendorToDto(vendor);

        return ResponseEntity.ok().body(vendorDto);
    }

    @GetMapping("/findVendorByBusinessEntity/{entity}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<VendorDto>> getVendorByBusinessEntity(@PathVariable(value = "entity")BusinessEntity businessEntity)
                                                                                        throws VendorNotFoundException {

        return ResponseEntity.ok().body(vendorService.findVendorByBusinessEntity(businessEntity)
                .stream()
                .map(this::convertVendorToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findAllVendors")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<VendorDto>> getAllVendors(){

        return ResponseEntity.ok().body(vendorService.findAllVendors(10)
                .stream()
                .map(this::convertVendorToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("findVendorByUsername/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<VendorDto> getVendorByUsername(@PathVariable(value = "username") String username) throws AppUserNotFoundException {

        AppUser appUser=appUserService.findUserByUsername(username);

        Vendor vendor=vendorService.findVendorByUsername(appUser,username);

        VendorDto vendorDto=convertVendorToDto(vendor);

        return ResponseEntity.ok().body(vendorDto);
    }

    @PutMapping("/updateVendorById/{id}")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<ModifyVendor> updateVendorById(@Valid @RequestBody ModifyVendor modifyVendor,
                                                         @PathVariable(value = "id") Long id) throws VendorNotFoundException {

        Vendor vendor=modelMapper.map(modifyVendor,Vendor.class);

        Vendor updateVendor=vendorService.updateVendorById(vendor,id);

        ModifyVendor posted=modelMapper.map(updateVendor,ModifyVendor.class);

        return ResponseEntity.ok().body(posted);
    }

    @DeleteMapping("/deleteVendorById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteVendorById(@PathVariable(value = "id") Long id) throws VendorNotFoundException {

        vendorService.deleteVendorById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllVendors")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllVendors(){

        vendorService.deleteAllVendors();

        return ResponseEntity.noContent().build();
    }


    private VendorDto convertVendorToDto(Vendor vendor){
        VendorDto vendorDto=new VendorDto();

       vendorDto.setId(vendor.getId());
       vendorDto.setVendorCode(vendor.getVendorCode());
       vendorDto.setBusinessEntity(vendor.getBusinessEntity());
       vendorDto.setName(vendor.getName());
       vendorDto.setRcNumber(vendor.getRcNumber());
       vendorDto.setTaxId(vendor.getTaxId());
       vendorDto.setNatureOfBusiness(vendor.getNatureOfBusiness());
       vendorDto.setMeansOfIdentification(vendor.getMeansOfIdentification());
       vendorDto.setMeansOfIdNumber(vendor.getMeansOfIdNumber());
       vendorDto.setMeansOfIdIssueDate(vendor.getMeansOfIdIssueDate());
       vendorDto.setMeansOfIdExpiryDate(vendor.getMeansOfIdExpiryDate());
       vendorDto.setRepresentative(vendor.getRepresentative());
       vendorDto.setNatureOfBusiness(vendor.getNatureOfBusiness());
       vendorDto.setPhone(vendor.getAppUser().getPhone());

        return vendorDto;
    }
}
