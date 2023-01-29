package com.logicgate.seller.service;



import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.seller.exception.SellerNotFoundException;
import com.logicgate.seller.model.Seller;
import com.logicgate.staticdata.BusinessEntityType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;

import java.util.List;

public interface SellerService {
    Seller addSeller(Seller seller) throws UserRoleNotFoundException, AppUserNotFoundException, SellerNotFoundException;
    Seller fetchSellerById(Long id) throws SellerNotFoundException;
    Seller fetchSellerByCodeOrNameOrCompanyRepOrTaxIdOrRcNumber(String searchKey) throws SellerNotFoundException;
    List<Seller> fetchAllSellerOrByBusinessEntityType(BusinessEntityType businessEntityType, Integer pageNumber);
    Seller fetchSellerByEmailOrUsernameOrPhone(String searchKey) throws AppUserNotFoundException;
    Seller updateSeller(Seller seller, Long id) throws SellerNotFoundException;
    void deleteSeller(Long id) throws SellerNotFoundException;
    void deleteAllSellers();
}
