package com.farmmart.service.service;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.services.Service;
import com.farmmart.data.model.services.ServiceNotFoundException;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;

import java.util.List;

public interface ServiceService {

    Service saveService(Service service);
    Service findServiceById(Long id) throws ServiceNotFoundException;
    Service findServiceByName(String name) throws ServiceNotFoundException;
    List<Service> findAllServices(Integer limit);
    List<Service> findServiceByCategory(Category category, String categoryName) throws CategoryNotFoundException;
    List<Service> findServiceByVendor(Vendor vendor, String vendorName) throws VendorNotFoundException;
    Service updateService(Service service,Long id) throws ServiceNotFoundException;
    void deleteServiceById(Long id) throws ServiceNotFoundException;
    void deleteAllServices();
}
