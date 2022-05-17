package com.farmmart.service.localgovernment;

import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.data.repository.localgovernment.LocalGovernmentRepository;
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
public class LocalGovernmentServiceImpl implements LocalGovernmentService{

    @Autowired
    private LocalGovernmentRepository localGovernmentRepository;

    @Autowired
    private StateRepository stateRepository;

    @Override
    public LocalGovernment saveLocalGovernment(LocalGovernment localGovernment) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernmentName=localGovernmentRepository.findByLocalGovernmentName(localGovernment.getLocalGovernmentName());

        if (localGovernmentName!=null){
            throw new LocalGovernmentNotFoundException("Local Govt. already exist");
        }
        return localGovernmentRepository.save(localGovernment);
    }

    @Override
    public LocalGovernment findLocalGovernmentById(Long id) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernment=localGovernmentRepository.findById(id)
                .orElseThrow(()->new LocalGovernmentNotFoundException("Local Govt. Not Found"));

        return localGovernment;
    }

    @Override
    public LocalGovernment findLocalGovernmentByName(String localGovernmentName) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernment=localGovernmentRepository.findByLocalGovernmentName(localGovernmentName);

        if (localGovernment==null){
            throw new LocalGovernmentNotFoundException("Local Govt. Not Found");
        }
        return localGovernment;
    }

    @Override
    public List<LocalGovernment> findAllLocalGovernment(Integer limit) {
        List<LocalGovernment> localGovernments=localGovernmentRepository.findAll(PageRequest
                .of(0,limit)).toList();

        return localGovernments;
    }

    @Override
    public List<LocalGovernment> findLocalGovernmentByState(States state, String stateName) {
        state=stateRepository.findByStateName(stateName);

        return localGovernmentRepository.findLocalGovernmentByState(state);
    }

    @Override
    public LocalGovernment updateLocalGovernment(LocalGovernment localGovernment, Long id) throws LocalGovernmentNotFoundException {
        LocalGovernment saveLocalGovt=localGovernmentRepository.findById(id)
                .orElseThrow(()->new LocalGovernmentNotFoundException("Local Govt. Not Found"));

        if (localGovernment.getLocalGovernmentName()!=null){
            saveLocalGovt.setLocalGovernmentName(localGovernment.getLocalGovernmentName());
        }if (localGovernment.getState()!=null){
            saveLocalGovt.setState(localGovernment.getState());
        }
        log.info("Local Govt.{} is updated",id);

        return localGovernmentRepository.save(saveLocalGovt);
    }

    @Override
    public void deleteLocalGovernmentById(Long id) throws LocalGovernmentNotFoundException {
       localGovernmentRepository.deleteById(id);

       Optional<LocalGovernment> optionalLocalGovernment=localGovernmentRepository.findById(id);

       if (optionalLocalGovernment.isPresent()){
           throw new LocalGovernmentNotFoundException("Local Govt. "+id+" is Not Deleted");
       }
    }

    @Override
    public void deleteAllLocalGovernment() {

        localGovernmentRepository.deleteAll();
    }
}
