package com.logicgate.colour.service;


import com.logicgate.colour.exception.ColourNotFoundException;
import com.logicgate.colour.model.Colour;
import com.logicgate.colour.repository.ColourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class ColourServiceImpl implements ColourService{
    @Autowired
    private ColourRepository colourRepository;

    @Override
    public Colour addColour(Colour colour) throws ColourNotFoundException {
        Optional<Colour> savedColour=colourRepository.findByColourNameOrCode(colour.getColourName(),colour.getColourCode());
        if (savedColour.isPresent()){
            throw new ColourNotFoundException("Colour is taken");
        }
        colour.setColourCode("COLOUR".concat(String.valueOf(new Random().nextInt(100000))));
        return colourRepository.save(colour);
    }

    @Override
    public Colour fetchColourById(Long id) throws ColourNotFoundException {
        return colourRepository.findById(id).orElseThrow(()->new ColourNotFoundException("Colour ID "+id+" Not Found"));
    }

    @Override
    public Colour fetchColourByNameOrCode(String searchKey) throws ColourNotFoundException {
        return colourRepository.findByColourNameOrCode(searchKey, searchKey)
                .orElseThrow(()->new ColourNotFoundException("Colour code"+searchKey+" Not Found"));
    }

    @Override
    public List<Colour> fetchAllColours(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        return colourRepository.findAll(pageable).toList();
    }

    @Override
    public Colour updateColour(Colour colour, Long id) throws ColourNotFoundException {
        Colour savedColour=colourRepository.findById(id)
                .orElseThrow(()->new ColourNotFoundException("Colour ID "+id+" Not Found"));
        if (Objects.nonNull(colour.getColourName()) && !"".equalsIgnoreCase(colour.getColourName())){
            savedColour.setColourName(colour.getColourName());
        }
        return colourRepository.save(savedColour);
    }

    @Override
    public void deleteColour(Long id) throws ColourNotFoundException {
        if (colourRepository.existsById(id)){
            colourRepository.deleteById(id);
        }else {
            throw new ColourNotFoundException("Colour ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllColours() {
        colourRepository.deleteAll();
    }
}
