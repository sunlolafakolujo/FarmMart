package com.logicgate.farmservices.service;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.farmservices.exception.FarmServiceNotFoundException;
import com.logicgate.farmservices.model.FarmService;
import com.logicgate.seller.exception.SellerNotFoundException;

import java.util.List;

public interface FarmServices {
    FarmService addService(FarmService farmService) throws AppUserNotFoundException;
    FarmService fetchServiceById(Long id) throws FarmServiceNotFoundException;
    FarmService fetchServiceByCode(String serviceCode) throws FarmServiceNotFoundException;
    List<FarmService> fetchServiceBySeller(String searchKey) throws SellerNotFoundException;
    List<FarmService> fetchServiceByCategory(String searchKey) throws CategoryNotFoundException;
    List<FarmService> fetchAllServiceOrByNameOrDescription(String searchKey, Integer pageNumber);
    FarmService updateService(FarmService farmService,Long id) throws FarmServiceNotFoundException;
    void deleteServiceBySeller() throws AppUserNotFoundException;
    void deleteServiceById(Long id) throws FarmServiceNotFoundException;
    void deleteServiceByCode(String serviceCode) throws AppUserNotFoundException;
    void deleteAllServices();
}
