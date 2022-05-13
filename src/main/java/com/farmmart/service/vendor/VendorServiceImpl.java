package com.farmmart.service.vendor;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.userrole.UserRoleRepository;
import com.farmmart.data.repository.vendor.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class VendorServiceImpl implements VendorService{
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public Vendor saveVendor(Vendor vendor) throws VendorNotFoundException {

        Vendor name=vendorRepository.findByName(vendor.getName());

        Vendor rcNumber=vendorRepository.findByRcNumber(vendor.getRcNumber());

        if (name!=null || rcNumber!=null){
            throw new VendorNotFoundException("Vendor already exist");
        }

        return vendorRepository.save(vendor);
    }

    @Override
    public Vendor findVendorById(Long id) throws VendorNotFoundException {
        Vendor vendor=vendorRepository.findById(id).orElseThrow(()->new VendorNotFoundException("Vendor Not Found"));

        return vendor;
    }

    @Override
    public Vendor findVendorByName(String name) throws VendorNotFoundException {
        Vendor vendor=vendorRepository.findByName(name);

        if (name==null){
            throw new VendorNotFoundException("Vendor Not Found");
        }
        return vendor;
    }

    @Override
    public Vendor findVendorByRCNumber(String rcNumber) throws VendorNotFoundException {
        Vendor vendor= vendorRepository.findByRcNumber(rcNumber);

        if (vendor==null){
            throw new VendorNotFoundException("Vendor Not Found");
        }
        return vendor;
    }

    @Override
    public List<Vendor> findVendorByBusinessEntity(BusinessEntity businessEntity) throws VendorNotFoundException {

        List<Vendor> businessEnt=vendorRepository.findByBusinessEntity(businessEntity);

        if (businessEnt.isEmpty()){
            throw new VendorNotFoundException("Vendor Not Found");
        }
        return businessEnt;
    }

    @Override
    public List<Vendor> findAllVendors() {

        return vendorRepository.findAll();
    }

    @Override
    public Vendor findVendorByUsername(AppUser appUser, String username) {
        appUser=appUserRepository.findByUserName(username);

        Vendor vendor=vendorRepository.findByUsername(appUser);

        return vendor;
    }

    @Override
    public Vendor updateVendorById(Vendor vendor, Long id) throws VendorNotFoundException {
        Vendor savedVendor=vendorRepository.findById(id).orElseThrow(()->new VendorNotFoundException("Vendor Not Found"));

        if (Objects.nonNull(vendor.getMeansOfIdentification())){
            savedVendor.setMeansOfIdentification(vendor.getMeansOfIdentification());
        }if (Objects.nonNull(vendor.getMeansOfIdNumber()) && !"".equalsIgnoreCase(vendor.getMeansOfIdNumber())){
            savedVendor.setMeansOfIdNumber(vendor.getMeansOfIdNumber());
        }if (Objects.nonNull(vendor.getMeansOfIdIssueDate())){
            savedVendor.setMeansOfIdIssueDate(vendor.getMeansOfIdIssueDate());
        }if (Objects.nonNull(vendor.getMeansOfIdExpiryDate())){
            savedVendor.setMeansOfIdExpiryDate(vendor.getMeansOfIdExpiryDate());
        }if (Objects.nonNull(vendor.getFacility())){
            savedVendor.setFacility(vendor.getFacility());
        }if (Objects.nonNull(vendor.getRepresentative()) && !"".equalsIgnoreCase(vendor.getRepresentative())){
            savedVendor.setRepresentative(vendor.getRepresentative());
        }

        return vendorRepository.save(savedVendor);
    }

    @Override
    public void deleteVendorById(Long id) throws VendorNotFoundException {

        vendorRepository.deleteById(id);

        Optional<Vendor> optionalVendor=vendorRepository.findById(id);

        if (optionalVendor.isPresent()){
            throw new VendorNotFoundException("Vendor Not Deleted");
        }
    }

    @Override
    public void deleteAllVendors() {

        vendorRepository.deleteAll();
    }
}
