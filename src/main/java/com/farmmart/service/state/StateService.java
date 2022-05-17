package com.farmmart.service.state;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;

import java.util.List;

public interface StateService {
    States saveState(States state) throws StateNotFoundException;
    States findStateById(Long id) throws StateNotFoundException;
    States findStateByName(String stateName) throws StateNotFoundException;
    List<States> findAllStates(Integer limit);
    List<States> findStatesByCountry(Country country,String countryName) throws CountryNotFoundException;
    States updateState(States states,Long id) throws StateNotFoundException;
    void deleteStateById(Long id) throws StateNotFoundException;
    void deleteAllStates();
}
