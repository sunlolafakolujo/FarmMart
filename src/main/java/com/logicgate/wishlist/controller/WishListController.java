package com.logicgate.wishlist.controller;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.wishlist.exception.WishListNotFoundException;
import com.logicgate.wishlist.model.NewWishList;
import com.logicgate.wishlist.model.WishList;
import com.logicgate.wishlist.model.WishListDto;
import com.logicgate.wishlist.service.WishListService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class WishListController {
    private final WishListService wishListService;
    private final ModelMapper modelMapper;

    @PostMapping("/addProductToWishList")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<NewWishList> addProductToWishList(@RequestBody NewWishList newWishList) throws WishListNotFoundException, AppUserNotFoundException {
        WishList wishList=modelMapper.map(newWishList,WishList.class);
        WishList post=wishListService.addProductToWishList(wishList);
        NewWishList posted=modelMapper.map(post,NewWishList.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findWishListByBuyer")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<WishListDto>> getBuyerWishList() throws AppUserNotFoundException {
        return new ResponseEntity<>(wishListService.fetchWishListByBuyer()
                .stream()
                .map(this::convertWishListToDto)
                .collect(Collectors.toList()), OK);
    }

    @DeleteMapping("/deleteWishList")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> deleteWishListById(@RequestParam("id") Long id) throws WishListNotFoundException {
        wishListService.deleteWishList(id);
        return ResponseEntity.ok().body("WishList Id "+id+" is deleted");
    }

    @DeleteMapping("/deleteBuyerWishList")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> deleteBuyerWishList() throws AppUserNotFoundException {
        wishListService.deleteAllWishList();
        return ResponseEntity.ok().body("Buyer WishList is deleted");
    }

    private WishListDto convertWishListToDto(WishList wishList){
        WishListDto wishListDto=new WishListDto();
        wishListDto.setId(wishList.getId());
        wishListDto.setBuyerName(wishList.getBuyer().getFirstName().concat(" ").concat(wishList.getBuyer().getLastName()));
        wishListDto.setProductType(wishList.getProduct().getProductType());
        wishListDto.setProductName(wishList.getProduct().getProductName());
        wishListDto.setProductDescription(wishList.getProduct().getProductDescription());
        wishListDto.setProductPrice(wishList.getProduct().getPrice());
        return wishListDto;
    }
}
