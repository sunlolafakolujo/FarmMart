package com.farmmart.service.country;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.repository.country.CountryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class CountryServiceImplTest {

    @MockBean
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService=new CountryServiceImpl();

    Country country;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        country=new Country();
    }

    @Test
    void testThatYouCanMockSaveCountryMethod() throws CountryNotFoundException {

        Mockito.when(countryRepository.save(country)).thenReturn(country);

        countryService.saveCountry(country);

        ArgumentCaptor<Country> countryArgumentCaptor=ArgumentCaptor.forClass(Country.class);

        Mockito.verify(countryRepository,Mockito.times(1)).save(countryArgumentCaptor.capture());

        Country capturedCountry=countryArgumentCaptor.getValue();

        assertEquals(capturedCountry,country);
    }

    @Test
    void testThatYouCanMockFindCountryByIdMethod() throws CountryNotFoundException {
        Long id=3L;
        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(country));

        country=countryService.findCountryById(id);

        Mockito.verify(countryRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindCountryByNameMethod() throws CountryNotFoundException {
        String countryName="Ghana";
        Mockito.when(countryRepository.findByCountyName(countryName)).thenReturn(country);

        country=countryService.findCountryByName(countryName);

        Mockito.verify(countryRepository,Mockito.times(1)).findByCountyName(countryName);
    }

    @Test
    void testThatYouCanMockFindAllCountryMethod() {
        List<Country> countries=new ArrayList<>();

        Mockito.when(countryRepository.findAll()).thenReturn(countries);

        countryService.findAllCountries(10);

        Mockito.verify(countryRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockUpdateCountryMethod() throws CountryNotFoundException {
        Long id=1L;
        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(country));

        countryService.updateCountry(country,id);

        Mockito.verify(countryRepository,Mockito.times(1)).save(country);
    }

    @Test
    void testThatYouCanMockDeleteCountryByIdMethod() throws CountryNotFoundException {
        Long id=2L;

        doNothing().when(countryRepository).deleteById(id);

        countryService.deleteCountryById(id);

        verify(countryRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCMockDeleteAllCountryMethod() {
        doNothing().when(countryRepository).deleteAll();

        countryService.deleteAllCountry();

        verify(countryRepository,times(1)).deleteAll();
    }
}