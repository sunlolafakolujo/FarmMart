package com.farmmart.data.repository.vendor;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.staticdata.Facility;
import com.farmmart.data.model.staticdata.MeansOfIdentification;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.data.repository.address.AddressRepository;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.localgovernment.LocalGovernmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Slf4j
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class VendorRepositoryTest {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private LocalGovernmentRepository localGovernmentRepository;

    Vendor vendor;

    AppUser appUser;

    Address address;

    LocalGovernment localGovernment;

    @BeforeEach
    void setUp() {
        vendor=new Vendor();
        appUser=new AppUser();
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatYouCanSaveVendor(){
        String name="Amuwo Odofin";
        localGovernment=localGovernmentRepository.findByLocalGovernmentName(name);

        address.setStreetNumber("100");
        address.setStreetName("Ojo Road");
        address.setCity("Maza Maza");
        address.setLandMark("Abule Ado Bus Stop");
        address.setPostZipCode("111001");
        address.setLocalGovernment(localGovernment);
        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        appUser.setUsername("Dapafol");
        appUser.setPassword("dapafol_123#");
        appUser.setPhone("08077865412");
        appUser.setEmail("dapafol@live.com");
        appUser.setAddresses(addresses);

        vendor.setAppUser(appUser);
        vendor.setBusinessEntity(BusinessEntity.BUSINESS);
        vendor.setName("Dapafol Integrated Services Limited");
        vendor.setRcNumber("26789087");
        vendor.setTaxId("12689");
        vendor.setMeansOfIdentification(MeansOfIdentification.DRIVERS_LICENSE);
        vendor.setMeansOfIdNumber("1257890074320");
        vendor.setMeansOfIdIssueDate(LocalDate.parse("2018-06-10"));
        vendor.setMeansOfIdExpiryDate(LocalDate.parse("2022-06-09"));
        vendor.setRepresentative("Kunle Akingbade");
        vendor.setNatureOfBusiness("Supply of Agricultural Equipment");
        vendor.setFacility(Facility.OWN);

        log.info("Vendor repo before saving {}",vendor);

        assertDoesNotThrow(()->vendorRepository.save(vendor));

        log.info("Vendor repo after saving {}",vendor);
    }

    @Test
    void testThatYouCanFindVendorById() throws VendorNotFoundException {
        Long id=2L;
        vendor=vendorRepository.findById(id).orElseThrow(()->new VendorNotFoundException("Vendor Not Found"));

        assertEquals(id,vendor.getId());

        log.info("Vendor Id 2 {}",vendor.getName());
    }

    @Test
    void testThatYouCanFindVendorByName() throws VendorNotFoundException {
        String name="Logic Gate";

        Vendor vendors=vendorRepository.findByName(name);

        if (vendors==null){
            throw new VendorNotFoundException("Vendor Name Not Found");
        }

        log.info("Vendor {}",vendors);
    }

    @Test
    void testThatYouCanFindVendorByBusinessEntity() throws VendorNotFoundException {
        BusinessEntity businessEntity=BusinessEntity.BUSINESS;

        List<Vendor>vendors=vendorRepository.findByBusinessEntity(businessEntity);

        if (vendors.isEmpty()){
            throw new VendorNotFoundException("Vendor Entity Not Found");
        }

        log.info("Vendors {}",vendors);
    }

//    @Test
//    void testThatYouCanFindVendorByStatus() throws VendorNotFoundException {
//        Status status= Status.ACTIVE;
//
//        List<Vendor> vendors=vendorRepository.findByStatus(status);
//
//        if (vendors.isEmpty()){
//            throw new VendorNotFoundException("Vendor Status Not Found");
//        }
//
//        log.info("Vendors {}",vendors);
//    }

    @Test
    void testThatYouCanFindAllVendors(){
        List<Vendor> vendors=vendorRepository.findAll();

        log.info("Vendors {}",vendors);
    }

    @Test
    void testThatYouCanUpdateVendorDetails() throws VendorNotFoundException {
        Long id=1L;
        vendor=vendorRepository.findById(id).orElseThrow(()->new VendorNotFoundException("Vendor Not Found"));

        MeansOfIdentification meansOfIdentification= MeansOfIdentification.NIN;

        vendor.setMeansOfIdentification(meansOfIdentification);

        assertDoesNotThrow(()->vendorRepository.save(vendor));

        assertEquals(meansOfIdentification,vendor.getMeansOfIdentification());

        log.info("Vendor status{}",vendor);
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteVendorById() throws VendorNotFoundException {
        Long id=1L;
        vendorRepository.deleteById(id);

        Optional<Vendor> optionalVendor=vendorRepository.findById(id);

        if (optionalVendor.isPresent()){
            throw new VendorNotFoundException("Vendor Is Not Deleted");
        }
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteAllVendors(){
        vendorRepository.deleteAll();
    }
}