package com.farmmart.controller.countryrestcontroller;

import com.farmmart.data.model.country.*;
import com.farmmart.service.country.CountryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/country")
@RequiredArgsConstructor
@Slf4j
public class CountryResController {

    private final CountryServiceImpl countryService;

    private final ModelMapper modelMapper;

    @PostMapping("/saveCountry")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewCountry> saveCountry(@Valid @RequestBody NewCountry newCountry) throws CountryNotFoundException {

        Country country=modelMapper.map(newCountry,Country.class);

        Country postCountry=countryService.saveCountry(country);

        NewCountry postedCountry=modelMapper.map(postCountry,NewCountry.class);

        return new ResponseEntity<>(postedCountry, HttpStatus.CREATED);
    }

    @GetMapping("/findCountryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable(value = "id") Long id) throws CountryNotFoundException {
        Country country=countryService.findCountryById(id);

        CountryDto countryDto=modelMapper.map(country,CountryDto.class);

        return ResponseEntity.ok().body(countryDto);
    }

    @GetMapping("/findCountryByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CountryDto> getCountryByName(@PathVariable(value = "name") String countryName) throws CountryNotFoundException {
        Country country=countryService.findCountryByName(countryName);

        CountryDto countryDto=modelMapper.map(country,CountryDto.class);

        return ResponseEntity.ok().body(countryDto);
    }

    @GetMapping("/findAllCountries")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CountryDto>> getAllCountry(){

        return ResponseEntity.ok().body(countryService.findAllCountries()
                .stream()
                .map(country -> modelMapper.map(country,CountryDto.class))
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateCountryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyCountry> modifyCountry(@Valid @RequestBody ModifyCountry modifyCountry,@PathVariable(value = "id") Long id) throws CountryNotFoundException {

        log.info("Update Country By ID");

        Country country=modelMapper.map(modifyCountry,Country.class);

        Country updateCountry=countryService.updateCountry(country,id);

        ModifyCountry modifiedCountry=modelMapper.map(updateCountry,ModifyCountry.class);

        return ResponseEntity.ok().body(modifiedCountry);
    }

    @DeleteMapping("/deleteCountryById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCountryById(@PathVariable(value = "id")Long id) throws CountryNotFoundException {

        log.info("Deleting Country By ID");
        countryService.deleteCountryById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllCountries")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllCountries(){

        log.info("Deleting All Countries");

        countryService.deleteAllCountry();

        return ResponseEntity.noContent().build();
    }
}
