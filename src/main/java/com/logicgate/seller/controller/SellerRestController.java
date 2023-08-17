package com.logicgate.seller.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.event.RegistrationEvent;
import com.logicgate.image.model.Picture;
import com.logicgate.seller.exception.SellerNotFoundException;
import com.logicgate.seller.model.NewSeller;
import com.logicgate.seller.model.Seller;
import com.logicgate.seller.model.SellerDto;
import com.logicgate.seller.model.UpdateSeller;
import com.logicgate.seller.service.SellerService;
import com.logicgate.staticdata.BusinessEntityType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SellerRestController {
    private final SellerService sellerService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher publisher;

    @PostMapping(value = {"/addSeller"},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewSeller> addSeller(@RequestPart("seller") NewSeller newSeller,
                                               @RequestPart("taxCacDocument") MultipartFile[] files,
                                               HttpServletRequest request) throws AppUserNotFoundException,
                                                            SellerNotFoundException, UserRoleNotFoundException {
        List<Picture> taxCacDocuments=new ArrayList<>();
        try {
            taxCacDocuments=uploadTaxCacDocuments(files);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        newSeller.setTaxCacDocuments(taxCacDocuments);
        Seller seller=modelMapper.map(newSeller, Seller.class);
        Seller post=sellerService.addSeller(seller);
        publisher.publishEvent(new RegistrationEvent(post.getAppUser(), applicationUrl(request)));
        NewSeller posted=modelMapper.map(post,NewSeller.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findSellerById")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<SellerDto> getSellerById(@RequestParam("id") Long id) throws SellerNotFoundException {
        Seller seller=sellerService.fetchSellerById(id);
        return new ResponseEntity<>(convertSellerToDto(seller), OK);
    }

    @GetMapping("/findSellerByCodeOrNameOrCompanyRepTaxOrCacNumber")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<SellerDto> getSellByCodeOrNameOrCompRepOrTaxOrCacNumber(@RequestParam("searchKey") String searchKey)
                                                                                  throws SellerNotFoundException {
        Seller seller= sellerService.fetchSellerByCodeOrNameOrCompanyRepOrTaxIdOrRcNumber(searchKey);
        return new ResponseEntity<>(convertSellerToDto(seller),OK);
    }

    @GetMapping("/findAllSellerOrByBusinessEntityType")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<List<SellerDto>> getAllSellerOrByBusinessEntityType(@RequestParam("pageNumber") Integer pageNumber,
                                                                              @RequestParam("businessEntityType") BusinessEntityType businessEntityType){
        return new ResponseEntity<>(sellerService.fetchAllSellerOrByBusinessEntityType(businessEntityType,pageNumber)
                .stream()
                .map(this::convertSellerToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findSellerByEmailOrPhoneOrUsername")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<SellerDto> getSellerByUsernameOrEmailOrPhone(@RequestParam("searchKey") String searchKey) throws AppUserNotFoundException {
        Seller seller=sellerService.fetchSellerByEmailOrUsernameOrPhone(searchKey);
        return new ResponseEntity<>(convertSellerToDto(seller),OK);
    }

    @PutMapping("/updateSeller")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<UpdateSeller> editSeller(@RequestBody UpdateSeller updateSeller,
                                                   @RequestParam("id") Long id) throws SellerNotFoundException {
        Seller seller=modelMapper.map(updateSeller,Seller.class);
        Seller post=sellerService.updateSeller(seller,id);
        UpdateSeller posted=modelMapper.map(post,UpdateSeller.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteSeller")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<?> deleteSellerById(@RequestParam("id") Long id) throws SellerNotFoundException {
        sellerService.deleteSeller(id);
        return ResponseEntity.ok().body("Seller ID "+id+" Is Deleted");
    }

    @DeleteMapping("/deleteAllSellers")
    @PreAuthorize("hasRole('BUSINESS_DEVELOPER')")
    public ResponseEntity<?> deleteAllSellers(){
        sellerService.deleteAllSellers();
        return ResponseEntity.ok().body("All Sellers has being deleted");
    }




    private List<Picture> uploadTaxCacDocuments(MultipartFile[] multipartFiles) throws IOException {
        List<Picture> pictures=new ArrayList<>();
        for (MultipartFile file:multipartFiles){
            Picture picture=new Picture(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes());
            pictures.add(picture);
        }
        return pictures;
    }

    private String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    private SellerDto convertSellerToDto(Seller seller){
        SellerDto sellerDto=new SellerDto();
        sellerDto.setId(seller.getId());
        sellerDto.setSellerCode(seller.getSellerCode());
        sellerDto.setSellerName(seller.getSellerName());
        sellerDto.setBusinessEntityType(seller.getBusinessEntityType());
        sellerDto.setNatureOfBusiness(seller.getNatureOfBusiness());
        sellerDto.setCompanyRepresentative(seller.getCompanyRepresentative());
        sellerDto.setTaxId(seller.getTaxId());
        sellerDto.setRcNumber(seller.getRcNumber());
        sellerDto.setFacilityOwnership(seller.getFacilityOwnership());
        sellerDto.setMobile(seller.getAppUser().getMobile());
        sellerDto.setEmail(seller.getAppUser().getEmail());
        sellerDto.setStreetNumber(seller.getAppUser().getContact().getHouseNumber());
        sellerDto.setStreetName(seller.getAppUser().getContact().getStreetName());
        sellerDto.setCity(seller.getAppUser().getContact().getCity());
        sellerDto.setLandmark(seller.getAppUser().getContact().getLandmark());
        sellerDto.setStateProvince(seller.getAppUser().getContact().getStateProvince());
        sellerDto.setCountry(seller.getAppUser().getContact().getCountry());
        sellerDto.setTaxCacDocuments(seller.getTaxCacDocuments());
        return sellerDto;
    }
}
