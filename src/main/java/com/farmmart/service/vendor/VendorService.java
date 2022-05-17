package com.farmmart.service.vendor;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;

import java.util.List;

public interface VendorService {
    Vendor saveVendor(Vendor vendor) throws VendorNotFoundException;
    Vendor findVendorById(Long id) throws VendorNotFoundException;
    Vendor findVendorByName(String name) throws VendorNotFoundException;
    Vendor findVendorByRCNumber(String rcNumber) throws VendorNotFoundException;
    List<Vendor> findVendorByBusinessEntity(BusinessEntity businessEntity) throws VendorNotFoundException;
    List<Vendor> findAllVendors(Integer limit);
    Vendor findVendorByUsername(AppUser appUser, String username);
    Vendor updateVendorById(Vendor vendor, Long id) throws VendorNotFoundException;
    void deleteVendorById(Long id) throws VendorNotFoundException;
    void deleteAllVendors();
}
