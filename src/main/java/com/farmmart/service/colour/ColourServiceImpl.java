package com.farmmart.service.colour;

import com.farmmart.data.model.colour.Colour;
import com.farmmart.data.model.colour.ColourNotFoundException;
import com.farmmart.data.repository.colour.ColourRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ColourServiceImpl implements ColourService{

    @Autowired
    private ColourRepository colourRepository;


    @Override
    public Colour saveColour(Colour colour) throws ColourNotFoundException {

        return colourRepository.save(colour);
    }

    @Override
    public Colour findColourById(Long id) throws ColourNotFoundException {

        Colour colour=colourRepository.findById(id).orElseThrow(()->new ColourNotFoundException("Colour Not Found"));

        return colour;
    }

    @Override
    public Colour findColourByName(String name) throws ColourNotFoundException {
        Colour colourName=colourRepository.findColourByName(name);

        if (colourName==null){
            throw new ColourNotFoundException("Colour Not Found");
        }
        return colourName;
    }

    @Override
    public List<Colour> findAllColours(Integer limit) {

        List<Colour> colours =colourRepository.findAll();

        return colours;
    }

    @Override
    public Colour updateColour(Colour colour, Long id) throws ColourNotFoundException {

        Colour savedColour=colourRepository.findById(id).orElseThrow(()->new ColourNotFoundException("Colour Not Found"));

        if (Objects.nonNull(colour.getColourName()) || !"".equalsIgnoreCase(colour.getColourName())){
            savedColour.setColourName(colour.getColourName());
        }

        return colourRepository.save(savedColour);
    }

    @Override
    public void deleteColourById(Long id) throws ColourNotFoundException {
        colourRepository.deleteById(id);

        Optional<Colour> optionalColour=colourRepository.findById(id);

        if (optionalColour.isPresent()){
            throw new ColourNotFoundException("Colour id "+id+" is not deleted");
        }

    }

    @Override
    public void deleteAllColours() {

        colourRepository.deleteAll();

    }
}
