package com.farmmart.service.localgovernment;

import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.state.States;

import java.util.List;

public interface LocalGovernmentService {
    LocalGovernment saveLocalGovernment(LocalGovernment localGovernment) throws LocalGovernmentNotFoundException;
    LocalGovernment findLocalGovernmentById(Long id) throws LocalGovernmentNotFoundException;
    LocalGovernment findLocalGovernmentByName(String localGovernmentName) throws LocalGovernmentNotFoundException;
    List<LocalGovernment> findAllLocalGovernment(Integer limit);
    List<LocalGovernment> findLocalGovernmentByState(States state, String stateName);
    LocalGovernment updateLocalGovernment(LocalGovernment localGovernment,Long id) throws LocalGovernmentNotFoundException;
    void deleteLocalGovernmentById(Long id) throws LocalGovernmentNotFoundException;
    void deleteAllLocalGovernment();
}
