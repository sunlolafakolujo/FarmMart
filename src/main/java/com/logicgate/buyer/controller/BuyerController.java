package com.logicgate.buyer.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.buyer.exception.BuyerNotFoundException;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.model.BuyerDto;
import com.logicgate.buyer.model.NewBuyer;
import com.logicgate.buyer.model.UpdateBuyer;
import com.logicgate.buyer.service.BuyerService;
import com.logicgate.event.RegistrationEvent;
import com.logicgate.staticdata.Gender;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BuyerController {
    public final BuyerService buyerService;
    public final ModelMapper modelMapper;
    public final ApplicationEventPublisher publisher;

    @PostMapping("/addBuyer")
    public ResponseEntity<NewBuyer> addBuyer(@RequestBody NewBuyer newBuyer,
                                             HttpServletRequest request)
                                             throws UserRoleNotFoundException, AppUserNotFoundException,
                                                                                        BuyerNotFoundException {
        Buyer buyer=modelMapper.map(newBuyer,Buyer.class);
        Buyer post=buyerService.addBuyer(buyer);
        publisher.publishEvent(new RegistrationEvent(post.getAppUser(), applicationUrl(request)));
        NewBuyer posted=modelMapper.map(post,NewBuyer.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findBuyerById")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BuyerDto> getBuyerById(@RequestParam("id") Long id) throws BuyerNotFoundException {
        Buyer buyer=buyerService.fetchBuyerById(id);
        return new ResponseEntity<>(convertBuyerToDto(buyer),OK);
    }

    @GetMapping("/findBuyerByCode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BuyerDto> getBuyerByCode(@RequestParam("code") String buyerCode) throws BuyerNotFoundException {
        Buyer buyer=buyerService.fetchBuyerByCode(buyerCode);
        return new ResponseEntity<>(convertBuyerToDto(buyer),OK);
    }

    @GetMapping("/findBuyerByGender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BuyerDto>> getBuyerByGender(@RequestParam("gender") Gender gender,
                                                           @RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(buyerService.fetchBuyerByGender(gender,pageNumber)
                .stream()
                .map(this::convertBuyerToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findBuyerWithinAgeRange")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BuyerDto>> getBuyerWithinAgeRange(@RequestParam("minAge") Integer minAge,
                                                           @RequestParam("maxAge") Integer maxAge,
                                                           @RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(buyerService.fetchBuyerWithinAgeRange(minAge,maxAge,pageNumber)
                .stream()
                .map(this::convertBuyerToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findBuyerWithinAgeLimit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BuyerDto>> getBuyerWithinAgeLimit(@RequestParam("age") Integer age,
                                                                 @RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(buyerService.fetchBuyerWithinAgeLimit(age,pageNumber)
                .stream()
                .map(this::convertBuyerToDto)
                .collect(Collectors.toList()),OK);
    }

    @GetMapping("/findBuyerByEmailOrMobileOrUsername")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BuyerDto> getBuyerByEmailUserOrMobile(@RequestParam("searchKey") String searchKey)
                                                                        throws AppUserNotFoundException {
        Buyer buyer=buyerService.fetchBuyerByUsernameOrEmailOrMobile(searchKey);
        return new ResponseEntity<>(convertBuyerToDto(buyer),OK);
    }

    @GetMapping("/findAllBuyersOrByFirstLastOrOtherName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BuyerDto>> getAllBuyersOrByFirstOrLastOrOtherName(@RequestParam("pageNumber") Integer pageNumber,
                                                                                 @RequestParam("searchKey")String searchKey){
        return new ResponseEntity<>(buyerService.fetchAllBuyerOrByFirstOrLastOrOtherName(searchKey,pageNumber)
                .stream()
                .map(this::convertBuyerToDto)
                .collect(Collectors.toList()), OK);
    }

    @PutMapping("/updateBuyer")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<UpdateBuyer> editBuyer(@RequestBody UpdateBuyer updateBuyer,
                                                 @RequestParam("id") Long id) throws BuyerNotFoundException {
        Buyer buyer=modelMapper.map(updateBuyer,Buyer.class);
        Buyer post=buyerService.updateBuyer(buyer,id);
        UpdateBuyer posted=modelMapper.map(post,UpdateBuyer.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteBuyer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBuyerById(@RequestParam("id") Long id) throws BuyerNotFoundException {
        buyerService.deleteBuyer(id);
        return ResponseEntity.ok().body("Buyer with ID "+id+" Is Deleted");
    }

    @DeleteMapping("/deleteAllBuyers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllBuyers(){
        buyerService.deleteAllBuyer();
        return ResponseEntity.ok().body("Buyers are deleted");
    }




    private String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }



    private BuyerDto convertBuyerToDto(Buyer buyer){
        BuyerDto buyerDto=new BuyerDto();
        buyerDto.setId(buyer.getId());
        buyerDto.setBuyerCode(buyer.getBuyerCode());
        buyerDto.setFirstName(buyer.getFirstName());
        buyerDto.setLastName(buyer.getLastName());
        buyerDto.setOtherName(buyer.getOtherName());
        buyerDto.setGender(buyer.getGender());
        buyerDto.setDateOfBirth(buyer.getDateOfBirth());
        buyerDto.setAge(buyer.getAge());
        buyerDto.setEmail(buyer.getAppUser().getEmail());
        buyerDto.setMobile(buyer.getAppUser().getMobile());
        buyerDto.setHouseNumber(buyer.getAppUser().getContact().getHouseNumber());
        buyerDto.setStreetName(buyer.getAppUser().getContact().getStreetName());
        buyerDto.setLandmark(buyer.getAppUser().getContact().getLandmark());
        buyerDto.setCity(buyer.getAppUser().getContact().getCity());
        buyerDto.setStateProvince(buyer.getAppUser().getContact().getStateProvince());
        buyerDto.setCountry(buyer.getAppUser().getContact().getCountry());
        return buyerDto;
    }
}
