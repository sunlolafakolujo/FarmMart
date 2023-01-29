package com.logicgate.wishlist.service;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.wishlist.exception.WishListNotFoundException;
import com.logicgate.wishlist.model.WishList;
import com.logicgate.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishListServiceImpl implements WishListService{
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public WishList addProductToWishList(WishList wishList) throws AppUserNotFoundException, WishListNotFoundException {
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        List<WishList> buyerWishList=wishListRepository.findWishListByBuyer(buyer);
        List<WishList> filteredWishList=buyerWishList.stream().filter(x->x.getProduct().getId()
                .equals(wishList.getProduct().getId())).collect(Collectors.toList());
        if (filteredWishList.size()>0){
            throw new WishListNotFoundException("Product already exist in your wishlist");
        }
        return wishListRepository.save(wishList);
    }

    @Override
    public List<WishList> fetchWishListByBuyer() throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        return wishListRepository.findWishListByBuyer(buyer);
    }

    @Override
    public void deleteWishList(Long id) throws WishListNotFoundException {
        if (wishListRepository.existsById(id)){
            wishListRepository.deleteById(id);
        }else {
            throw new WishListNotFoundException("Wishlist ID "+id+" Not Found");
        }

    }

    @Override
    public void deleteAllWishList() throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        List<WishList> wishLists=wishListRepository.findWishListByBuyer(buyer);
        wishListRepository.deleteAll(wishLists);
    }
}
