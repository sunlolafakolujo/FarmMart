package com.farmmart.service.vendor;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.data.repository.appuser.AppUserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private VendorService vendorService=new VendorServiceImpl();

    Vendor vendor;

    AppUser appUser;

    private
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vendor=new Vendor();
        appUser=new AppUser();
    }

    @Test
    void testThatYouCanMockSaveVendorMethod() throws VendorNotFoundException {
        Mockito.when(vendorRepository.save(vendor)).thenReturn(vendor);

        vendorService.saveVendor(vendor);

        ArgumentCaptor<Vendor> vendorArgumentCaptor=ArgumentCaptor.forClass(Vendor.class);

        Mockito.verify(vendorRepository,Mockito.times(1)).save(vendorArgumentCaptor.capture());

        Vendor capturedVendor=vendorArgumentCaptor.getValue();

        assertEquals(capturedVendor,vendor);
    }

    @Test
    void testThatYouCanMockFindVendorByIdMethod() throws VendorNotFoundException {
        Long id=1L;
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendor));

        vendorService.findVendorById(id);

        Mockito.verify(vendorRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindVendorByNameMethod() throws VendorNotFoundException {
        String name="Logic Gate";

        Mockito.when(vendorRepository.findByName(name)).thenReturn(vendor);

        vendorService.findVendorByName(name);

        Mockito.verify(vendorRepository,Mockito.times(1)).findByName(name);
    }

    @Test
    void testThatYouCanMockFindVendorByBusinessEntityMethod() throws VendorNotFoundException {
        BusinessEntity businessEntity=BusinessEntity.BUSINESS;

        List<Vendor> vendors=new ArrayList<>();
        Mockito.when(vendorRepository.findByBusinessEntity(businessEntity)).thenReturn(vendors);

        vendorService.findVendorByBusinessEntity(businessEntity);

        Mockito.verify(vendorRepository,Mockito.times(1)).findByBusinessEntity(businessEntity);
    }

//    @Test
//    void testThatYouCanMockFindVendorByStatusMethod() throws VendorNotFoundException {
//        Status vendorStatus= Status.INACTIVE;
//
//        List<Vendor> vendors=new ArrayList<>();
//
//        Mockito.when(vendorRepository.findByStatus(vendorStatus)).thenReturn(vendors);
//
//        vendorService.findVendorByStatus(vendorStatus);
//
//        Mockito.verify(vendorRepository,Mockito.times(1)).findByStatus(vendorStatus);
//    }

    @Test
    void testThatYouCanMockFindAllVendorsMethod() {
        List<Vendor> vendors=new ArrayList<>();

        Mockito.when(vendorRepository.findAll()).thenReturn(vendors);

        vendorService.findAllVendors(10);

        Mockito.verify(vendorRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockFindVendorByUsernameMethod() {
        String username="LogicGate";
        appUser=appUserRepository.findByUserName(username);

        Mockito.when(vendorRepository.findByUsername(appUser)).thenReturn(vendor);

        vendorService.findVendorByUsername(appUser,username);

        Mockito.verify(vendorRepository,Mockito.times(1)).findByUsername(appUser);
    }

    @Test
    void testThatYouCanMockUpdateVendorByIdMethod() throws VendorNotFoundException {
        Long id=2L;
        Mockito.when(vendorRepository.findById(id)).thenReturn(Optional.of(vendor));

        vendorService.updateVendorById(vendor,id);

        Mockito.verify(vendorRepository,Mockito.times(1)).save(vendor);
    }

    @Test
    void testThatYouCanMockDeleteVendorByIdMethod() throws VendorNotFoundException {
        Long id=2L;
        doNothing().when(vendorRepository).deleteById(id);

        vendorService.deleteVendorById(id);

        verify(vendorRepository,times(1)).deleteById(id);
    }

    @Test
    void deleteAllVendors() {
        doNothing().when(vendorRepository).deleteAll();

        vendorService.deleteAllVendors();

        verify(vendorRepository,times(1)).deleteAll();
    }
}