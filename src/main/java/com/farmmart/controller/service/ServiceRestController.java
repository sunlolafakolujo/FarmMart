package com.farmmart.controller.servicerestcontroller;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.services.*;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.service.category.CategoryServiceImp;
import com.farmmart.service.service.ServiceImpl;
import com.farmmart.service.vendor.VendorServiceImpl;
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
@RequestMapping(path = "/api/service")
@RequiredArgsConstructor
public class ServiceRestController {

    private final ServiceImpl serviceimpl;

    private final VendorServiceImpl vendorService;

    private final CategoryServiceImp categoryServiceImp;

    private final ModelMapper modelMapper;

    @PostMapping("/saveService")
    @PreAuthorize("hasAnyAuthority('ROLE_VENDOR','ROLE_ADMIN')")
    public ResponseEntity<NewService> saveService(@Valid @RequestBody NewService newService){

        Service service=modelMapper.map(newService, Service.class);

        Service post=serviceimpl.saveService(service);

        NewService postedService=modelMapper.map(post, NewService.class);

        return new ResponseEntity<>(postedService, HttpStatus.CREATED);
    }

    @GetMapping("/findServiceById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable(value = "id") Long id) throws ServiceNotFoundException {
        Service service=serviceimpl.findServiceById(id);

        ServiceDto serviceDto=convertServiceToDto(service);

        return ResponseEntity.ok().body(serviceDto);
    }

    @GetMapping("/findServiceByName/{name}")
    public ResponseEntity<ServiceDto> getServiceByName(@PathVariable(value = "name") String serviceName) throws ServiceNotFoundException {
        Service service=serviceimpl.findServiceByName(serviceName);

        ServiceDto serviceDto=convertServiceToDto(service);

        return ResponseEntity.ok().body(serviceDto);
    }

    @GetMapping("/findAllServices")
    public ResponseEntity<List<ServiceDto>> getAllServices(){

        return ResponseEntity.ok().body(serviceimpl.findAllServices()
                .stream()
                .map(this::convertServiceToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findServiceByCategory/{name}")
    public ResponseEntity<List<ServiceDto>> getServiceByCategory(@PathVariable(value = "name") String categoryName) throws CategoryNotFoundException {
        Category category=categoryServiceImp.findCategoryByName(categoryName);

        return ResponseEntity.ok().body(serviceimpl.findServiceByCategory(category, categoryName)
                .stream()
                .map(this::convertServiceToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findServiceByVendor/{name}")
    public ResponseEntity<List<ServiceDto>> getServiceByVendor(@PathVariable(value = "name") String vendorName) throws VendorNotFoundException {
        Vendor vendor=vendorService.findVendorByName(vendorName);

        return ResponseEntity.ok().body(serviceimpl.findServiceByVendor(vendor, vendorName)
                .stream()
                .map(this::convertServiceToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateService/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_VENDOR','ROLE_ADMIN')")
    public ResponseEntity<ModifyService> updateService(@Valid @RequestBody ModifyService modifyService,
                                                       @PathVariable(value = "id") Long id) throws ServiceNotFoundException {

        Service service=modelMapper.map(modifyService, Service.class);

        Service updateService=serviceimpl.updateService(service, id);

        ModifyService postUpdate=modelMapper.map(updateService, ModifyService.class);

        return ResponseEntity.ok().body(postUpdate);
    }

    @DeleteMapping("/deleteServiceById/{id}")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> deleteServiceById(@PathVariable(value = "id") Long id) throws ServiceNotFoundException {
        serviceimpl.deleteServiceById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllServices")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllServices(){
        serviceimpl.deleteAllServices();

        return ResponseEntity.noContent().build();
    }


    private ServiceDto convertServiceToDto(Service service){

        ServiceDto serviceDto=new ServiceDto();

        serviceDto.setId(service.getId());
        serviceDto.setServiceName(service.getServiceName());
        serviceDto.setServiceDescription(service.getServiceDescription());
        serviceDto.setCategoryName(service.getCategory().getCategoryName());
        serviceDto.setVendorName(service.getVendor().getName());

        return serviceDto;
    }
}
