package com.farmmart.controller.localgovernmentrestcontroller;

import com.farmmart.data.model.localgovernment.*;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
import com.farmmart.service.state.StateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/localGovernment")
@RequiredArgsConstructor
public class LocalGovernmentRestController {

    private final LocalGovernmentServiceImpl localGovernmentService;
    private final StateServiceImpl stateService;
    private final ModelMapper modelMapper;

    @PostMapping("/saveLocalGovernment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewLocalGovernment> saveLocalGovernment(@Valid @RequestBody NewLocalGovernment newLocalGovernment)
                                                                throws LocalGovernmentNotFoundException {

        LocalGovernment localGovernment=modelMapper.map(newLocalGovernment,LocalGovernment.class);

        LocalGovernment postLocalGovernment=localGovernmentService.saveLocalGovernment(localGovernment);

        NewLocalGovernment postedLocalGovt=modelMapper.map(postLocalGovernment,NewLocalGovernment.class);

        return new ResponseEntity<>(postedLocalGovt, HttpStatus.CREATED);
    }

    @GetMapping("/findLocalGovernmentById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LocalGovernmentDto> getLocalGovernmentById(@PathVariable(value = "id") Long id) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernment=localGovernmentService.findLocalGovernmentById(id);

        LocalGovernmentDto localGovernmentDto=convertLocalGovernmentToDto(localGovernment);

        return ResponseEntity.ok().body(localGovernmentDto);
    }

    @GetMapping("/findLocalGovernmentByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<LocalGovernmentDto> getLocalGovernmentByName(@PathVariable(value = "name") String localGovernmentName) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernment=localGovernmentService.findLocalGovernmentByName(localGovernmentName);

        LocalGovernmentDto localGovernmentDto=convertLocalGovernmentToDto(localGovernment);

        return ResponseEntity.ok().body(localGovernmentDto);
    }

    @GetMapping("/findAllLocalGovernments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LocalGovernmentDto>> getAllLocalGovernment() {
        return ResponseEntity.ok().body(localGovernmentService.findAllLocalGovernment()
                .stream()
                .map(this::convertLocalGovernmentToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findStateLocalGovernments/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LocalGovernmentDto>> getLocalGovernmentsByState(@PathVariable(value = "name") String stateName)
                                                                                throws StateNotFoundException {
        States state=stateService.findStateByName(stateName);

        return ResponseEntity.ok().body(localGovernmentService.findLocalGovernmentByState(state,stateName)
                .stream()
                .map(this::convertLocalGovernmentToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateLocalGovernmentById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyLocalGovernment> updateLocalGovernment(@Valid @RequestBody ModifyLocalGovernment modifyLocalGovernment,
                                                                       @PathVariable(value = "id") Long id) throws LocalGovernmentNotFoundException {
        LocalGovernment localGovernment=modelMapper.map(modifyLocalGovernment,LocalGovernment.class);

        LocalGovernment updateLocalGovt=localGovernmentService.updateLocalGovernment(localGovernment,id);

        ModifyLocalGovernment updatedLocalGovt=modelMapper.map(updateLocalGovt,ModifyLocalGovernment.class);

        return ResponseEntity.ok().body(updatedLocalGovt);
    }

    @DeleteMapping("/deleteLocalGovernmentById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteLocalGovernmentById(@PathVariable(value = "id") Long id) throws LocalGovernmentNotFoundException {
        localGovernmentService.deleteLocalGovernmentById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllLocalGovernments")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllLocalGovernment(){
        localGovernmentService.deleteAllLocalGovernment();

        return ResponseEntity.noContent().build();
    }

    private LocalGovernmentDto convertLocalGovernmentToDto(LocalGovernment localGovernment){

        LocalGovernmentDto localGovernmentDto=new LocalGovernmentDto();

        localGovernmentDto.setId(localGovernment.getId());
        localGovernmentDto.setLocalGovernmentName(localGovernment.getLocalGovernmentName());
        localGovernmentDto.setState(localGovernment.getState());

        return localGovernmentDto;
    }
}
