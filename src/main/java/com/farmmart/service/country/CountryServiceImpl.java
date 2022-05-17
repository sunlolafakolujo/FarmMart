package com.farmmart.service.country;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.repository.country.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService{
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Country saveCountry(Country country) throws CountryNotFoundException {
        Country countryName=countryRepository.findByCountyName(country.getCountryName());

        if (countryName!=null){
            throw new CountryNotFoundException("Country "+countryName+" already exist");
        }
        return countryRepository.save(country);
    }

    @Override
    public Country findCountryById(Long id) throws CountryNotFoundException {
        Optional<Country> country=countryRepository.findById(id);

        if (!country.isPresent()){
            throw new CountryNotFoundException("COUNTRY NOT FOUND");
        }

        return country.get();
    }

    @Override
    public Country findCountryByName(String countryName) throws CountryNotFoundException {
        Country country=countryRepository.findByCountyName(countryName);

        if (country==null){
            throw new CountryNotFoundException("Country does not exist");
        }
        return country;
    }

    @Override
    public List<Country> findAllCountries(Integer limit) {
        return countryRepository.findAll(PageRequest.of(0, limit)).toList();
    }

//    @Override
//    public Page<Country> findAllCountry(Pageable page) {
//
//        return countryRepository.findAll(page);
//    }

    @Override
    public Country updateCountry(Country country,Long id) throws CountryNotFoundException {
        Country savedCountry=countryRepository.findById(id).orElseThrow(()->new CountryNotFoundException("Country Not Found"));

        if(Objects.nonNull(country.getCountryName()) && !"".equalsIgnoreCase(country.getCountryName())){
            savedCountry.setCountryName(country.getCountryName());
        }
        return countryRepository.save(savedCountry);
    }

    @Override
    public void deleteCountryById(Long id) throws CountryNotFoundException {
        countryRepository.deleteById(id);

        Optional<Country> optionalCountry=countryRepository.findById(id);

        if (optionalCountry.isPresent()){
            throw new CountryNotFoundException("Country is not deleted");
        }
    }

    @Override
    public void deleteAllCountry() {
        countryRepository.deleteAll();
    }
}
