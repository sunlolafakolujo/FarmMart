package com.farmmart.service.state;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.data.repository.country.CountryRepository;
import com.farmmart.data.repository.state.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class StateServiceImplTest {

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateService stateService=new StateServiceImpl();

    @Mock
    private CountryRepository countryRepository;

    States state;

    Country country;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        state=new States();
        country=new Country();
    }

    @Test
    void testThatYouCanMockSaveStateMethod() throws StateNotFoundException {
        Mockito.when(stateRepository.save(state)).thenReturn(state);

        stateService.saveState(state);

        ArgumentCaptor<States> statesArgumentCaptor=ArgumentCaptor.forClass(States.class);

        Mockito.verify(stateRepository,Mockito.times(1)).save(statesArgumentCaptor.capture());

        States capturedState=statesArgumentCaptor.getValue();

        assertEquals(capturedState,state);
    }

    @Test
    void testThatYouCanMockFindStateByIdMethod() throws StateNotFoundException {
        Long id=3L;
        Mockito.when(stateRepository.findById(id)).thenReturn(Optional.of(state));

        stateService.findStateById(id);

        Mockito.verify(stateRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindStateByNameMethod() throws StateNotFoundException {
        String stateName="Ekiti";

        Mockito.when(stateRepository.findByStateName(stateName)).thenReturn(state);

        States state=stateService.findStateByName(stateName);

        Mockito.verify(stateRepository,Mockito.times(1)).findByStateName(stateName);
    }

    @Test
    void testThatYouCanMockFindAllStatesMethod() {
        List<States> statesList=new ArrayList<>();

        Mockito.when(stateRepository.findAll()).thenReturn(statesList);

        stateService.findAllStates(10);

        Mockito.verify(stateRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockFindStatesByCountry() throws CountryNotFoundException {

        String countryName="Nigeria";
        country=countryRepository.findByCountyName(countryName);

        List<States> statesList=new ArrayList<>();

        Mockito.when(stateRepository.findStateByCountry(country)).thenReturn(statesList);

        stateService.findStatesByCountry(country,countryName);

        Mockito.verify(stateRepository,Mockito.times(1)).findStateByCountry(country);
    }

    @Test
    void testThatYouCanMockUpdateStateMethod() throws StateNotFoundException {
        Long id =3L;

        Mockito.when(stateRepository.findById(id)).thenReturn(Optional.of(state));

        stateService.updateState(state,id);

        Mockito.verify(stateRepository,Mockito.times(1)).save(state);
    }

    @Test
    void testThatYouCanMockDeleteStateByIdMethod() throws StateNotFoundException {
        Long id=6L;
        doNothing().when(stateRepository).deleteById(id);

        stateService.deleteStateById(id);

        verify(stateRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllStatesMethod() {

        doNothing().when(stateRepository).deleteAll();

        stateService.deleteAllStates();

        verify(stateRepository,times(1)).deleteAll();
    }
}