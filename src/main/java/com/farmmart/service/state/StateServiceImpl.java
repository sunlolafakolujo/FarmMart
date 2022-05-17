package com.farmmart.service.state;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.data.repository.country.CountryRepository;
import com.farmmart.data.repository.state.StateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class StateServiceImpl implements StateService{
    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public States saveState(States state) throws StateNotFoundException {
        States savedState=stateRepository.findByStateName(state.getStateName());

        if (savedState!=null){
            throw new StateNotFoundException("State already exist");
        }

        return stateRepository.save(state);
    }

    @Override
    public States findStateById(Long id) throws StateNotFoundException {
        Optional<States> state=stateRepository.findById(id);

        if (state.isEmpty()){
            throw new StateNotFoundException("State Not Found");
        }
        return state.get();
    }

    @Override
    public States findStateByName(String stateName) throws StateNotFoundException {
        States state=stateRepository.findByStateName(stateName);

        if (state==null){
            throw new StateNotFoundException("State Not Found");
        }
        return state;
    }

    @Override
    public List<States> findAllStates(Integer limit) {
        return stateRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public List<States> findStatesByCountry(Country country, String countryName) throws CountryNotFoundException {
        country=countryRepository.findByCountyName(countryName);

        return stateRepository.findStateByCountry(country);
    }

    @Override
    public States updateState(States state, Long id) throws StateNotFoundException {
        States savedState=stateRepository.findById(id).orElseThrow(()->new StateNotFoundException("State Not Found"));

        if (state.getStateName()!=null){
            savedState.setStateName(state.getStateName());
        }if (state.getCountry()!=null){
            savedState.setCountry(state.getCountry());
        }
        return stateRepository.save(savedState);
    }

    @Override
    public void deleteStateById(Long id) throws StateNotFoundException {

        stateRepository.deleteById(id);

        Optional<States> optionalStates=stateRepository.findById(id);

        if (optionalStates.isPresent()){
            throw new StateNotFoundException("State Is Not Deleted");
        }
    }

    @Override
    public void deleteAllStates() {
        stateRepository.deleteAll();
    }
}
