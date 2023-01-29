package com.logicgate.buyer.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.exception.BuyerNotFoundException;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.staticdata.Gender;
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

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@Transactional
public class BuyerServiceImpl implements BuyerService{
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Buyer addBuyer(Buyer buyer) throws UserRoleNotFoundException, AppUserNotFoundException, BuyerNotFoundException {
        Optional<AppUser> appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(buyer.getAppUser().getUsername()
                ,buyer.getAppUser().getEmail(),buyer.getAppUser().getMobile());
        if (appUser.isPresent()){
            throw new BuyerNotFoundException("Username Or Email Or Mobile already exist");
        }if (!buyer.getAppUser().getPassword().equals(buyer.getAppUser().getConfirmPassword())){
            throw new AppUserNotFoundException("Password does not match");
        }if (!validatePhoneNumber(buyer)){
            throw new AppUserNotFoundException("Mobile phone must be in one of these formats: " +
                    "10 or 11 digit, 0000 000 0000, 000 000 0000, 000-000-0000, 000-000-0000 ext0000");
        }
        buyer.setBuyerCode("BUYER".concat(String.valueOf(new Random().nextInt(100000))));
        UserRole userRole=userRoleRepository.findByUserRoleName("BUYER")
                .orElseThrow(()->new UserRoleNotFoundException("Role Not Found"));
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);
        buyer.getAppUser().setUserRoles(userRoles);
        buyer.getAppUser().setUserCode("USER".concat(String.valueOf(new Random().nextInt(100000))));
        buyer.getAppUser().setUserType(UserType.BUYER);
        buyer.getAppUser().setPassword(passwordEncoder.encode(buyer.getAppUser().getPassword()));
        buyer.setAge(Period.between(buyer.getDateOfBirth(), LocalDate.now()).getYears());
        return buyerRepository.save(buyer);
    }

    @Override
    public Buyer fetchBuyerById(Long id) throws BuyerNotFoundException {
        return buyerRepository.findById(id)
                .orElseThrow(()->new BuyerNotFoundException("Buyer with ID "+id+" Not Found"));
    }

    @Override
    public Buyer fetchBuyerByCode(String buyerCode) throws BuyerNotFoundException {
        return buyerRepository.findBuyerByCode(buyerCode)
                .orElseThrow(()->new BuyerNotFoundException("Buyer with ID "+buyerCode+" Not Found"));
    }

    @Override
    public List<Buyer> fetchBuyerByGender(Gender gender, Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return buyerRepository.findBuyerByGender(gender,pageable);
    }

    @Override
    public List<Buyer> fetchBuyerWithinAgeRange(Integer minAge, Integer maxAge, Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        return buyerRepository.findByBuyerWithinAgeRange(minAge,maxAge,pageable);
    }

    @Override
    public List<Buyer> fetchBuyerWithinAgeLimit(Integer age, Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        return buyerRepository.findBuyerByAgeLimit(age,pageable);
    }

    @Override
    public List<Buyer> fetchAllBuyerOrByFirstOrLastOrOtherName(String searchKey, Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (searchKey.equals("")){
            return buyerRepository.findAll(pageable).toList();
        }else {
            return buyerRepository.findBuyerByFirstNameOrLastNameOtherName(searchKey, searchKey,searchKey,pageable);
        }
    }

    @Override
    public Long isBuyerPresent(Buyer buyer) throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        return buyer!=null?buyer.getId():null;
    }

    @Override
    public Buyer fetchBuyerByUsernameOrEmailOrMobile(String searchKey) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        return buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
    }

    @Override
    public Buyer updateBuyer(Buyer buyer, Long id) throws BuyerNotFoundException {
        Buyer savedBuyer=buyerRepository.findById(id)
                .orElseThrow(()->new BuyerNotFoundException("Buyer with ID "+id+" Not Found"));
        if (Objects.nonNull(buyer.getFirstName()) && !"".equalsIgnoreCase(buyer.getFirstName())){
            savedBuyer.setFirstName(buyer.getFirstName());
        }if (Objects.nonNull(buyer.getLastName()) && !"".equalsIgnoreCase(buyer.getLastName())){
            savedBuyer.setLastName(buyer.getLastName());
        }if (Objects.nonNull(buyer.getOtherName()) && !"".equalsIgnoreCase(buyer.getOtherName())){
            savedBuyer.setOtherName(buyer.getOtherName());
        }if (Objects.nonNull(buyer.getAppUser().getEmail()) && !"".equalsIgnoreCase(buyer.getAppUser().getEmail())){
            savedBuyer.getAppUser().setEmail(buyer.getAppUser().getEmail());
        }if (Objects.nonNull(buyer.getAppUser().getMobile()) && !"".equalsIgnoreCase(buyer.getAppUser().getMobile())){
            savedBuyer.getAppUser().setMobile(buyer.getAppUser().getMobile());
        }
        return buyerRepository.save(savedBuyer);
    }

    @Override
    public void deleteBuyer(Long id) throws BuyerNotFoundException {
        if (buyerRepository.existsById(id)){
            buyerRepository.deleteById(id);
        }else {
            throw new BuyerNotFoundException("Buyer with ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllBuyer() {
        buyerRepository.deleteAll();
    }

    private static boolean validatePhoneNumber(Buyer buyer) {
        // validate phone numbers of format "1234567890"
        if (buyer.getAppUser().getMobile().matches("\\d{10}")) {
            return true;
        }
        // validate phone numbers of format "12345678901"
        else if (buyer.getAppUser().getMobile().matches("\\d{11}")) {
            return true;
        }
        // validating phone number with -, . or spaces
        else if (buyer.getAppUser().getMobile().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }
        // validating phone number with extension length from 3 to 5
        else if (buyer.getAppUser().getMobile().matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        }
        // validating phone number where area code is in braces ()
        else if (buyer.getAppUser().getMobile().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }    // Validation for India numbers
        else if (buyer.getAppUser().getMobile().matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) {
            return true;
        } else if (buyer.getAppUser().getMobile().matches("\\(\\d{5}\\)-\\d{3}-\\d{3}")) {
            return true;
        } else if (buyer.getAppUser().getMobile().matches("\\(\\d{4}\\)-\\d{3}-\\d{3}")){
            return true;
        }    // return false if nothing matches the input
        else {
            return false;
        }
    }
}
