package com.logicgate.farmservices.controller;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.farmservices.exception.FarmServiceNotFoundException;
import com.logicgate.farmservices.model.FarmService;
import com.logicgate.farmservices.model.FarmServiceDto;
import com.logicgate.farmservices.model.NewFarmService;
import com.logicgate.farmservices.model.UpdateFarmService;
import com.logicgate.farmservices.service.FarmServices;
import com.logicgate.seller.exception.SellerNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
public class FarmServiceController {
    private final FarmServices farmServices;
    private final ModelMapper modelMapper;

    @PostMapping("/addFarmService")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<NewFarmService> addFarmService(@RequestBody NewFarmService newFarmService) throws AppUserNotFoundException {
        FarmService farmService=modelMapper.map(newFarmService,FarmService.class);
        FarmService post=farmServices.addService(farmService);
        NewFarmService posted=modelMapper.map(post,NewFarmService.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findService")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FarmServiceDto> getServiceById(@RequestParam("Id")Long id) throws FarmServiceNotFoundException {
        FarmService farmService=farmServices.fetchServiceById(id);
        return new ResponseEntity<>(convertFarmServiceToDto(farmService),OK);
    }

    @GetMapping("/findServiceByCode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FarmServiceDto> getServiceByCode(@RequestParam("serviceCode") String serviceCode)
                                                                                throws FarmServiceNotFoundException {
        FarmService farmService =farmServices.fetchServiceByCode(serviceCode);
        return new ResponseEntity<>(convertFarmServiceToDto(farmService),OK);
    }

    @GetMapping("/findAllServiceOrByNameOrDescription")
    public ResponseEntity<List<FarmServiceDto>> getAllServiceOrByNameOrDescription(@RequestParam("pageNumber") Integer pageNumber,
                                                                                   @RequestParam("searchKey")String searchKey){
        return new ResponseEntity<>(farmServices.fetchAllServiceOrByNameOrDescription(searchKey, pageNumber)
                .stream()
                .map(this::convertFarmServiceToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findServiceByCategory")
    public ResponseEntity<List<FarmServiceDto>> getServiceByCategory(@RequestParam("searchKey") String searchKey)
                                                                                    throws CategoryNotFoundException {
        return new ResponseEntity<>(farmServices.fetchServiceByCategory(searchKey)
                .stream()
                .map(this::convertFarmServiceToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findServiceBySeller")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FarmServiceDto>> getServicesBySeller(@RequestParam("searchKey") String searchKey)
                                                                                        throws SellerNotFoundException {
        return new ResponseEntity<>(farmServices.fetchServiceBySeller(searchKey)
                .stream()
                .map(this::convertFarmServiceToDto)
                .collect(Collectors.toList()),OK);
    }

    @PutMapping("/updateService")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<UpdateFarmService> editService(@RequestBody UpdateFarmService updateFarmService,
                                                       @RequestParam("id")Long id) throws FarmServiceNotFoundException {
        FarmService farmService=modelMapper.map(updateFarmService,FarmService.class);
        FarmService post=farmServices.updateService(farmService,id);
        UpdateFarmService posted=modelMapper.map(post,UpdateFarmService.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteAllSellerServices")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteSellerServices() throws AppUserNotFoundException {
        farmServices.deleteServiceBySeller();
        return ResponseEntity.ok().body("Seller services has being deleted");
    }

    @DeleteMapping("/deleteServiceById")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteServiceById(@RequestParam("id")Long id) throws FarmServiceNotFoundException {
        farmServices.deleteServiceById(id);
        return ResponseEntity.ok().body("Service ID "+id+" has being deleted");
    }

    @DeleteMapping("/deleteServiceByCode")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> deleteServiceByCode(@RequestParam("serviceCode")String serviceCode) throws AppUserNotFoundException {
        farmServices.deleteServiceByCode(serviceCode);
        return ResponseEntity.ok().body("Service ID "+serviceCode+" has being deleted");
    }

    @DeleteMapping("/deleteAllServices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllServices(){
        farmServices.deleteAllServices();
        return ResponseEntity.ok().body("All Services has being deleted");
    }

    private FarmServiceDto convertFarmServiceToDto(FarmService farmService){
        FarmServiceDto farmServiceDto=new FarmServiceDto();
        farmServiceDto.setId(farmService.getId());
        farmServiceDto.setServiceCode(farmService.getServiceCode());
        farmServiceDto.setServiceName(farmService.getServiceName());
        farmServiceDto.setServiceDescription(farmService.getServiceDescription());
        farmServiceDto.setCategories(farmService.getCategories());
        farmServiceDto.setSellerName(farmService.getSeller().getSellerName());
        farmServiceDto.setSellerMobile(farmService.getSeller().getAppUser().getMobile());
        farmServiceDto.setHouseNumber(farmService.getSeller().getAppUser().getContact().getHouseNumber());
        farmServiceDto.setStreetName(farmService.getSeller().getAppUser().getContact().getStreetName());
        farmServiceDto.setCity(farmService.getSeller().getAppUser().getContact().getCity());
        farmServiceDto.setLandmark(farmService.getSeller().getAppUser().getContact().getLandmark());
        farmServiceDto.setStateProvince(farmService.getSeller().getAppUser().getContact().getStateProvince());
        farmServiceDto.setCountry(farmService.getSeller().getAppUser().getContact().getCountry());


        return  farmServiceDto;
    }
}
