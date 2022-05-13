package com.farmmart.controller.colourrestcontroller;

import com.farmmart.data.model.colour.*;
import com.farmmart.service.colour.ColourServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/colour")
@RequiredArgsConstructor
public class ColourRestController {

    private final ColourServiceImpl colourService;

    private final ModelMapper modelMapper;

    @PostMapping("/saveColour")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<NewColour> saveColour(@RequestBody NewColour newColour) throws ColourNotFoundException {
        Colour colour =modelMapper.map(newColour, Colour.class);

        Colour savedColour=colourService.saveColour(colour);

        NewColour postSave=modelMapper.map(savedColour, NewColour.class);

        return new ResponseEntity<>(postSave, HttpStatus.CREATED);
    }

    @GetMapping("/findColourById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ColourDto> getColourById(@PathVariable(value = "id") Long id) throws ColourNotFoundException {

        Colour colour=colourService.findColourById(id);

        ColourDto colourDto=convertColourToDto(colour);

        return ResponseEntity.ok().body(colourDto);
    }

    @GetMapping("/findColourByName/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ColourDto> getColourByName(@PathVariable(value = "name") String name) throws ColourNotFoundException {

        Colour colour=colourService.findColourByName(name);

        ColourDto colourDto=convertColourToDto(colour);

        return ResponseEntity.ok().body(colourDto);
    }

    @GetMapping("/findAllColours")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<ColourDto>> getAllColours(){

        return ResponseEntity.ok().body(colourService.findAllColours()
                .stream()
                .map(this::convertColourToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("/updateColourById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ModifyColour> updateColour(@RequestBody ModifyColour modifyColour,@PathVariable(value = "id") Long id) throws ColourNotFoundException {
        Colour colour=modelMapper.map(modifyColour, Colour.class);

        Colour colourUpdate=colourService.updateColour(colour, id);

        ModifyColour postUpdate=modelMapper.map(colourUpdate, ModifyColour.class);

        return ResponseEntity.ok().body(postUpdate);
    }

    @DeleteMapping("/deleteColourById/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteColour(@PathVariable(value = "id") Long id) throws ColourNotFoundException {

        colourService.deleteColourById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllColours")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllColours(){

        colourService.deleteAllColours();

        return ResponseEntity.noContent().build();
    }


    private ColourDto convertColourToDto(Colour colour){

        ColourDto colourDto=new ColourDto();

        colourDto.setId(colour.getId());

        colourDto.setColourName(colour.getColourName());

        return colourDto;
    }
}
