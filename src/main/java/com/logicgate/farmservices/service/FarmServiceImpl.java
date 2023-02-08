package com.logicgate.farmservices.service;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.category.exception.CategoryNotFoundException;
import com.logicgate.category.model.Category;
import com.logicgate.category.repository.CategoryRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.farmservices.exception.FarmServiceNotFoundException;
import com.logicgate.farmservices.model.FarmService;
import com.logicgate.farmservices.repository.FarmServiceRepository;
import com.logicgate.seller.exception.SellerNotFoundException;
import com.logicgate.seller.model.Seller;
import com.logicgate.seller.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
public class FarmServiceImpl implements FarmServices{
    @Autowired
    private FarmServiceRepository farmServiceRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public FarmService addService(FarmService farmService) throws AppUserNotFoundException {
        farmService.setServiceCode("SER".concat(String.valueOf(new Random().nextInt(100000))));
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Seller seller= sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
        farmService.setSeller(seller);
        return farmServiceRepository.save(farmService);
    }

    @Override
    public FarmService fetchServiceById(Long id) throws FarmServiceNotFoundException {
        return farmServiceRepository.findById(id)
                .orElseThrow(()->new FarmServiceNotFoundException("Farm Service ID "+id+" Not Found"));
    }

    @Override
    public FarmService fetchServiceByCode(String serviceCode) throws FarmServiceNotFoundException {
        return farmServiceRepository.findByServiceCode(serviceCode)
                .orElseThrow(()->new FarmServiceNotFoundException("Farm Service code "+serviceCode+" Not Found"));
    }

    @Override
    public List<FarmService> fetchServiceBySeller(String searchKey) throws SellerNotFoundException {
//        Pageable pageable=PageRequest.of(pageNumber,10);
        Seller seller=sellerRepository.findSellerByCodeOrNameORepresentativeOrTaxIdOrRcNumber(searchKey,
                searchKey,searchKey,searchKey,searchKey)
                .orElseThrow(()->new SellerNotFoundException("Selle "+searchKey+" Not Found"));
        return farmServiceRepository.findServiceBySeller(seller);
    }

    @Override
    public List<FarmService> fetchServiceByCategory(String searchKey) throws CategoryNotFoundException {
        Category category=categoryRepository.findCategoryByCodeOrName(searchKey,searchKey)
                .orElseThrow(()->new CategoryNotFoundException("Category "+searchKey+" Not Found"));
        List<FarmService> farmServices=category.getFarmServices();
        return farmServices;
    }

    @Override
    public List<FarmService> fetchAllServiceOrByNameOrDescription(String searchKey, Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        if (searchKey.equals("")){
            return farmServiceRepository.findAll(pageable).toList();
        }else {
            return farmServiceRepository.findByServiceNameOrDescription(searchKey,searchKey,pageable);
        }
    }

    @Override
    public FarmService updateService(FarmService farmService, Long id) throws FarmServiceNotFoundException {
        FarmService savedService=farmServiceRepository.findById(id)
                .orElseThrow(()->new FarmServiceNotFoundException("Farm Service ID "+id+" Not Found"));
        if (Objects.nonNull(farmService.getServiceDescription()) && !"".equalsIgnoreCase(farmService.getServiceDescription())){
            savedService.setServiceDescription(farmService.getServiceDescription());
        }
        return farmServiceRepository.save(savedService);
    }

    @Override
    public void deleteServiceBySeller() throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Seller seller=sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
        List<FarmService> farmServices=farmServiceRepository.findServiceBySeller(seller);
        farmServiceRepository.deleteAll(farmServices);
    }

    @Override
    public void deleteServiceById(Long id) throws FarmServiceNotFoundException {
        if (farmServiceRepository.existsById(id)){
            farmServiceRepository.deleteById(id);
        }else {
            throw new FarmServiceNotFoundException("Farm Service with ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteServiceByCode(String serviceCode) throws AppUserNotFoundException {
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Seller seller=sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
        farmServiceRepository.deleteSellerServiceByCode(serviceCode,seller);
    }

    @Override
    public void deleteAllServices() {
        farmServiceRepository.deleteAll();
    }
}
