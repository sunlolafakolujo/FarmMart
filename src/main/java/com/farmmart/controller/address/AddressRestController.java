package com.farmmart.controller.address;

import com.farmmart.data.model.address.*;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.service.address.AddressServiceImpl;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
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
@RequestMapping(path = "/api/address")
@RequiredArgsConstructor
public class AddressRestController {

    private final AddressServiceImpl addressService;

    private final LocalGovernmentServiceImpl localGovernmentService;

    private final ModelMapper modelMapper;

    @PostMapping("/saveAddress")
    public ResponseEntity<NewAddress> saveAddres(@Valid @RequestBody NewAddress newAddress)
            throws AddressNotFoundException {

        Address address=modelMapper.map(newAddress,Address.class);

        Address postAddress=addressService.saveAddress(address);

        NewAddress postedAddress=modelMapper.map(postAddress,NewAddress.class);

        return new ResponseEntity<>(postedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/findAddressById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable(value = "id") Long id) throws AddressNotFoundException {
        Address address=addressService.findAddressById(id);

        AddressDto addressDto=convertToAddressDto(address);

        return ResponseEntity.ok().body(addressDto);
    }

    @GetMapping("/findAllAddresses")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AddressDto>> getAllAddresses(){
        return ResponseEntity.ok().body(addressService.findAllAddresses()
                .stream()
                .map(this::convertToAddressDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findAddressesByLocalGovernment/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AddressDto>> getAddressesByLocalGovernmentName(@PathVariable(value = "name") String localGovernmentName)
                                                                        throws LocalGovernmentNotFoundException {

        LocalGovernment localGovernment=localGovernmentService.findLocalGovernmentByName(localGovernmentName);

        return ResponseEntity.ok().body(addressService.findAddressesByLocalGovernment(localGovernment,localGovernmentName)
                .stream()
                .map(this::convertToAddressDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateAddressById/{id}")
    public ResponseEntity<ModifyAddress> updateAddress(@Valid @RequestBody ModifyAddress modifyAddress,
                                                       @PathVariable(value = "id") Long id)
                                                        throws AddressNotFoundException {

        Address address=modelMapper.map(modifyAddress,Address.class);

        Address updateAddress=addressService.updateAddress(address,id);

        ModifyAddress updatedAddress=modelMapper.map(updateAddress,ModifyAddress.class);

        return ResponseEntity.ok().body(updatedAddress);
    }

    @DeleteMapping("/deleteAddressById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAddressById(@PathVariable(value = "id") Long id) throws AddressNotFoundException {
        addressService.deleteAddressById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllAddresses")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllAddresses(){
        addressService.deleteAllAddresses();

        return ResponseEntity.noContent().build();
    }


    private AddressDto convertToAddressDto(Address address){

        AddressDto addressDto=new AddressDto();

        addressDto.setId(address.getId());
        addressDto.setStreetNumber(address.getStreetNumber());
        addressDto.setStreetName(address.getStreetName());
        addressDto.setCity(address.getCity());
        addressDto.setPostZipCode(address.getPostZipCode());
        addressDto.setLocalGovernmentName(address.getLocalGovernment().getLocalGovernmentName());
        addressDto.setStateName(address.getLocalGovernment().getState().getStateName());
        addressDto.setCountryName(address.getLocalGovernment().getState().getCountry().getCountryName());

        return addressDto;
    }
}
