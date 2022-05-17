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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private ServiceService serviceService=new ServiceImpl();

    Service service;
    Category category;
    Vendor vendor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service=new Service();
        category=new Category();
        vendor=new Vendor();
    }

    @Test
    void testThatYouCanMockSaveServiceMethod() {

        log.info("Mocking save service:{}", service);

        Mockito.when(serviceRepository.save(service)).thenReturn(service);

        serviceService.saveService(service);

        ArgumentCaptor<Service> serviceArgumentCaptor=ArgumentCaptor.forClass(Service.class);

        Mockito.verify(serviceRepository,Mockito.times(1)).save(serviceArgumentCaptor.capture());

        Service capturedService=serviceArgumentCaptor.getValue();

        assertEquals(capturedService,service);
    }

    @Test
    void testThatYouCanMockFindServiceByIdMethod() throws ServiceNotFoundException {
        log.info("Mocking find Service by id:{}", service);

        Long id =2L;

        Mockito.when(serviceRepository.findById(id)).thenReturn(Optional.of(service));

        serviceService.findServiceById(id);

        Mockito.verify(serviceRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindServiceByNameMethod() throws ServiceNotFoundException {
        log.info("Mocking find Service by name:{} ", service);

        String serviceByName="Grooming";

        Mockito.when(serviceRepository.findByServiceName(serviceByName)).thenReturn(service);

        serviceService.findServiceByName(serviceByName);

        Mockito.verify(serviceRepository, Mockito.times(1)).findByServiceName(serviceByName);
    }

    @Test
    void testThatYouCanMockFindAllServicesMethod() {

        List<Service> services=new ArrayList<>();

        Mockito.when(serviceRepository.findAll()).thenReturn(services);

        serviceService.findAllServices(10);

        Mockito.verify(serviceRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockFindServiceByCategoryMethod() throws CategoryNotFoundException {
        String categoryName="Composting";
        category=categoryRepository.findByCategoryName(categoryName);

        List<Service> services=new ArrayList<>();

        Mockito.when(serviceRepository.findServiceByCategory(category)).thenReturn(services);

        serviceService.findServiceByCategory(category,categoryName);

        Mockito.verify(serviceRepository, Mockito.times(1)).findServiceByCategory(category);

    }

    @Test
    void testThatYouCanMockFindServiceByVendorMethod() throws VendorNotFoundException {

        String vendorName="Zero Waste";

        vendor=vendorRepository.findByName(vendorName);

        List<Service> services=new ArrayList<>();

        Mockito.when(serviceRepository.findServiceByVendor(vendor)).thenReturn(services);

        serviceService.findServiceByVendor(vendor, vendorName);

        Mockito.verify(serviceRepository, Mockito.times(1)).findServiceByVendor(vendor);
    }

    @Test
    void testThatYouCanMockUpdateServiceMethod() throws ServiceNotFoundException {

        Long id=1L;

        Mockito.when(serviceRepository.findById(id)).thenReturn(Optional.of(service));

        serviceService.updateService(service, id);

        Mockito.verify(serviceRepository, Mockito.times(1)).save(service);
    }

    @Test
    void testThatYouCanMockDeleteServiceByIdMethod() throws ServiceNotFoundException {
        Long id=2L;

        doNothing().when(serviceRepository).deleteById(id);

        serviceService.deleteServiceById(id);

        verify(serviceRepository, times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllServicesMethod() {
        doNothing().when(serviceRepository).deleteAll();

        serviceService.deleteAllServices();

        verify(serviceRepository, times(1)).deleteAll();
    }
}