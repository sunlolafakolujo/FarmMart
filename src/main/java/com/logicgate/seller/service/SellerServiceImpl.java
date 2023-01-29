package com.logicgate.seller.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.seller.exception.SellerNotFoundException;
import com.logicgate.seller.model.Seller;
import com.logicgate.seller.repository.SellerRepository;
import com.logicgate.staticdata.BusinessEntityType;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.UserRole;
import com.logicgate.userrole.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Seller addSeller(Seller seller) throws UserRoleNotFoundException, AppUserNotFoundException, SellerNotFoundException {
        Optional<AppUser> appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(seller.getAppUser().getUsername(),
                seller.getAppUser().getEmail(),seller.getAppUser().getMobile());
        if (appUser.isPresent()){
            throw new AppUserNotFoundException("Username or Email or Mobile already exist");
        }if (!validatePhoneNumber(seller)){
            throw new SellerNotFoundException("Mobile phone must be in one of these formats: " +
                    "10 or 11 digit, 0000 000 0000, 000 000 0000, 000-000-0000, 000-000-0000 ext0000");
        }if (!seller.getAppUser().getPassword().equals(seller.getAppUser().getConfirmPassword())){
            throw new AppUserNotFoundException("Password does not match");
        }
        seller.setSellerCode("SELLER".concat(String.valueOf(new Random().nextInt(100000))));
        seller.getAppUser().setPassword(passwordEncoder.encode(seller.getAppUser().getPassword()));
        seller.getAppUser().setUserCode("USER".concat(String.valueOf(new Random().nextInt(100000))));
        seller.getAppUser().setUserType(UserType.SELLER);
        String roleName="SELLER";
        UserRole userRole=userRoleRepository.findByUserRoleName(roleName)
                .orElseThrow(()->new UserRoleNotFoundException("Role "+roleName+" Not Found"));
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);
        seller.getAppUser().setUserRoles(userRoles);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller fetchSellerById(Long id) throws SellerNotFoundException {
        return sellerRepository.findById(id).orElseThrow(()->new SellerNotFoundException("Seller ID "+id+" Not Found"));
    }

    @Override
    public Seller fetchSellerByCodeOrNameOrCompanyRepOrTaxIdOrRcNumber(String searchKey) throws SellerNotFoundException {
        return sellerRepository.findSellerByCodeOrNameORepresentativeOrTaxIdOrRcNumber(searchKey,searchKey,searchKey,
                searchKey,searchKey).orElseThrow(()->new SellerNotFoundException("Seller "+searchKey+" Not Found"));
    }

    @Override
    public List<Seller> fetchAllSellerOrByBusinessEntityType(BusinessEntityType businessEntityType, Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return sellerRepository.findSellerByBusinessEntityType(businessEntityType,pageable);
    }

    @Override
    public Seller fetchSellerByEmailOrUsernameOrPhone(String searchKey) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("Seller "+searchKey+" Not Found"));
        return sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
    }

    @Override
    public Seller updateSeller(Seller seller, Long id) throws SellerNotFoundException {
        Seller savedSeller=sellerRepository.findById(id)
                .orElseThrow(()->new SellerNotFoundException("Seller ID "+id+" Not Found"));
        if (Objects.nonNull(seller.getCompanyRepresentative()) && !"".equalsIgnoreCase(seller.getCompanyRepresentative())){
            savedSeller.setCompanyRepresentative(seller.getCompanyRepresentative());
        }if (Objects.nonNull(seller.getFacilityOwnership()) && !"".equals(seller.getFacilityOwnership())){
            savedSeller.setFacilityOwnership(seller.getFacilityOwnership());
        }
        return sellerRepository.save(savedSeller);
    }

    @Override
    public void deleteSeller(Long id) throws SellerNotFoundException {
        if (sellerRepository.existsById(id)){
            sellerRepository.deleteById(id);
        }else {
            throw new SellerNotFoundException("Seller with ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllSellers() {
        sellerRepository.deleteAll();
    }

    private static boolean validatePhoneNumber(Seller seller) {
        // validate phone numbers of format "1234567890"
        if (seller.getAppUser().getMobile().matches("\\d{10}")) {
            return true;
        }
        // validate phone numbers of format "12345678901"
        else if (seller.getAppUser().getMobile().matches("\\d{11}")) {
            return true;
        }
        // validating phone number with -, . or spaces
        else if (seller.getAppUser().getMobile().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }
        // validating phone number with extension length from 3 to 5
        else if (seller.getAppUser().getMobile().matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        }
        // validating phone number where area code is in braces ()
        else if (seller.getAppUser().getMobile().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }    // Validation for India numbers
        else if (seller.getAppUser().getMobile().matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) {
            return true;
        } else if (seller.getAppUser().getMobile().matches("\\(\\d{5}\\)-\\d{3}-\\d{3}")) {
            return true;
        } else if (seller.getAppUser().getMobile().matches("\\(\\d{4}\\)-\\d{3}-\\d{3}")){
            return true;
        }    // return false if nothing matches the input
        else {
            return false;
        }
    }
}
