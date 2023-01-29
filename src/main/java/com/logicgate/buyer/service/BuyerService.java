package com.logicgate.buyer.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.buyer.exception.BuyerNotFoundException;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.staticdata.Gender;
import com.logicgate.userrole.exception.UserRoleNotFoundException;

import java.util.List;

public interface BuyerService {
    Buyer addBuyer(Buyer buyer) throws UserRoleNotFoundException, AppUserNotFoundException, BuyerNotFoundException;
    Buyer fetchBuyerById(Long id) throws BuyerNotFoundException;
    Buyer fetchBuyerByCode(String buyerCode) throws BuyerNotFoundException;
    List<Buyer> fetchBuyerByGender(Gender gender, Integer pageNumber);
    List<Buyer> fetchBuyerWithinAgeRange(Integer minAge, Integer maxAge, Integer pageNumber);
    List<Buyer> fetchBuyerWithinAgeLimit(Integer age,Integer pageNumber);
    List<Buyer> fetchAllBuyerOrByFirstOrLastOrOtherName(String searchKey, Integer pageNumber);
    Long isBuyerPresent(Buyer buyer) throws AppUserNotFoundException;
    Buyer fetchBuyerByUsernameOrEmailOrMobile(String searchKey) throws AppUserNotFoundException;
    Buyer updateBuyer(Buyer buyer, Long id) throws BuyerNotFoundException;//Authenticate user.
    void deleteBuyer(Long id) throws BuyerNotFoundException;
    void deleteAllBuyer();
}
