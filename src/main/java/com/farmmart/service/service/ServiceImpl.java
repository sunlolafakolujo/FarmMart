package com.farmmart.service.service;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.services.Service;
import com.farmmart.data.model.services.ServiceNotFoundException;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.data.repository.category.CategoryRepository;
import com.farmmart.data.repository.service.ServiceRepository;
import com.farmmart.data.repository.vendor.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
@Slf4j
public class ServiceImpl implements ServiceService{

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public Service saveService(Service service) {

        return serviceRepository.save(service);
    }

    @Override
    public Service findServiceById(Long id) throws ServiceNotFoundException {

        Service service=serviceRepository.findById(id).orElseThrow(()->new ServiceNotFoundException("Service Not Found"));

        return service;
    }

    @Override
    public Service findServiceByName(String name) throws ServiceNotFoundException {
        Service service=serviceRepository.findByServiceName(name);

        if (service==null){
            throw new ServiceNotFoundException("Service Not Found");
        }

        return service;
    }

    @Override
    public List<Service> findAllServices(Integer limit) {
        return serviceRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public List<Service> findServiceByCategory(Category category, String categoryName) throws CategoryNotFoundException {

        category =categoryRepository.findByCategoryName(categoryName);

        return serviceRepository.findServiceByCategory(category);
    }

    @Override
    public List<Service> findServiceByVendor(Vendor vendor, String vendorName) throws VendorNotFoundException {

        vendor=vendorRepository.findByName(vendorName);

        return serviceRepository.findServiceByVendor(vendor);
    }

    @Override
    public Service updateService(Service service, Long id) throws ServiceNotFoundException {
        Service savedService=serviceRepository.findById(id).orElseThrow(()->new ServiceNotFoundException("Service Not Found"));

        if (Objects.nonNull(service.getServiceDescription()) || !"".equalsIgnoreCase(service.getServiceDescription())){
            savedService.setServiceDescription(service.getServiceDescription());
        }

        return serviceRepository.save(savedService);
    }

    @Override
    public void deleteServiceById(Long id) throws ServiceNotFoundException {
        serviceRepository.deleteById(id);

        Optional<Service> optionalService=serviceRepository.findById(id);

        if (optionalService.isPresent()){
            throw new ServiceNotFoundException("Service id "+id+" is not deleted");
        }
    }

    @Override
    public void deleteAllServices() {

        serviceRepository.deleteAll();
    }
}
