package com.farmmart.controller.staterestcontroller;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.model.state.*;
import com.farmmart.service.country.CountryServiceImpl;
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
@RequestMapping(path = "/api/state")
@RequiredArgsConstructor
public class StateRestController {

    private final StateServiceImpl stateService;

    private final CountryServiceImpl countryService;

    private final ModelMapper modelMapper;

    @PostMapping("/saveState")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewState> saveState(@Valid @RequestBody NewState newState) throws StateNotFoundException {

        States state=modelMapper.map(newState,States.class);

        States postState=stateService.saveState(state);

        NewState postedState=modelMapper.map(postState,NewState.class);

        return new ResponseEntity<>(postedState, HttpStatus.CREATED);
    }

    @GetMapping("/findStateById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StateDto> getStateById(@PathVariable(value = "id")Long id) throws StateNotFoundException {
        States state=stateService.findStateById(id);

        StateDto stateDto=convertStateToDto(state);

        return ResponseEntity.ok().body(stateDto);
    }

    @GetMapping("/findStateByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StateDto> getStateByName(@PathVariable(value = "name") String stateName) throws StateNotFoundException {
        States state=stateService.findStateByName(stateName);

        StateDto stateDto=convertStateToDto(state);

        return ResponseEntity.ok().body(stateDto);
    }

    @GetMapping("/findAllStates")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<StateDto>> getAllStates(){
        return ResponseEntity.ok().body(stateService.findAllStates()
                .stream()
                .map(this::convertStateToDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/findCountryStates/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<StateDto>> getCountryStates(@PathVariable(value = "name") String countryName)
                                                            throws CountryNotFoundException {

        Country country=countryService.findCountryByName(countryName);

        return ResponseEntity.ok().body(stateService.findStatesByCountry(country,country.getCountryName())
                .stream()
                .map(this::convertStateToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateState/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyState> modifyState(@Valid @RequestBody ModifyState modifyState,
                                                   @PathVariable(value ="id" ) Long id)
                                                    throws StateNotFoundException {

        States state=modelMapper.map(modifyState,States.class);

        States updateState=stateService.updateState(state,id);

        ModifyState modifiedState=modelMapper.map(updateState,ModifyState.class);

        return ResponseEntity.ok().body(modifiedState);
    }

    @DeleteMapping("/deleteStateById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteStateById(@PathVariable(value = "id") Long id) throws StateNotFoundException {
        stateService.deleteStateById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllStates")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllStates(){
        stateService.deleteAllStates();

        return ResponseEntity.noContent().build();
    }

    private StateDto convertStateToDto(States state){

        StateDto stateDto=new StateDto();

        stateDto.setId(state.getId());
        stateDto.setStateName(state.getStateName());
        stateDto.setCountry(state.getCountry());

        return stateDto;
    }
}
