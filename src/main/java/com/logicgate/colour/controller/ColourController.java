package com.logicgate.colour.controller;


import com.logicgate.colour.exception.ColourNotFoundException;
import com.logicgate.colour.model.Colour;
import com.logicgate.colour.model.ColourDto;
import com.logicgate.colour.model.NewColour;
import com.logicgate.colour.model.UpdateColour;
import com.logicgate.colour.service.ColourService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class ColourController {
    @Autowired
    private ColourService colourService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/addColour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NewColour> addColour(@RequestBody NewColour newColour) throws ColourNotFoundException {
        Colour colour=modelMapper.map(newColour,Colour.class);
        Colour post=colourService.addColour(colour);
        NewColour posted=modelMapper.map(post,NewColour.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findColourById")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColourDto> getColourById(@RequestParam("id") Long id) throws ColourNotFoundException {
        Colour colour=colourService.fetchColourById(id);
        return new ResponseEntity<>(convertColourToDto(colour),OK);
    }

    @GetMapping("/findColourByName")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColourDto> getColourByName(@RequestParam("searchKey") String searchKey) throws ColourNotFoundException {
        Colour colour=colourService.fetchColourByNameOrCode(searchKey);
        return new ResponseEntity<>(convertColourToDto(colour),OK);
    }

    @GetMapping("/findAllColours")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ColourDto>> getAllColours(@RequestParam("pageNumber")Integer pageNumber){
        return new ResponseEntity<>(colourService.fetchAllColours(pageNumber)
                .stream()
                .map(this::convertColourToDto)
                .collect(Collectors.toList()),OK);
    }

    @PutMapping("/updateColour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UpdateColour> editColour(@RequestBody UpdateColour updateColour,
                                                   @RequestParam("id")Long id) throws ColourNotFoundException {
        Colour colour=modelMapper.map(updateColour,Colour.class);
        Colour post=colourService.updateColour(colour,id);
        UpdateColour posted=modelMapper.map(post,UpdateColour.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteColour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteColourById(@RequestParam("id")Long id) throws ColourNotFoundException {
        colourService.deleteColour(id);
        return ResponseEntity.ok().body("Colour ID "+id+" IS Deleted");
    }

    @DeleteMapping("/deleteAllColour")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllColours(){
        colourService.deleteAllColours();
        return ResponseEntity.ok().body("Colours are Deleted");
    }

    private ColourDto convertColourToDto(Colour colour){
        ColourDto colourDto=new ColourDto();
        colourDto.setId(colour.getId());
        colourDto.setColourCode(colour.getColourCode());
        colourDto.setColourName(colour.getColourName());
        return colourDto;
    }
}
