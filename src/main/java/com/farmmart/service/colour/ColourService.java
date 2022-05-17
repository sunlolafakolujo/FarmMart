package com.farmmart.service.colour;

import com.farmmart.data.model.colour.Colour;
import com.farmmart.data.model.colour.ColourNotFoundException;

import java.util.List;

public interface ColourService {

    Colour saveColour(Colour colour) throws ColourNotFoundException;
    Colour findColourById(Long id) throws ColourNotFoundException;
    Colour findColourByName(String name) throws ColourNotFoundException;
    List<Colour> findAllColours(Integer limit);
    Colour updateColour(Colour colour, Long id) throws ColourNotFoundException;
    void deleteColourById(Long id) throws ColourNotFoundException;
    void deleteAllColours();
}
