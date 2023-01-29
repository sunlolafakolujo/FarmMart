package com.logicgate.colour.service;


import com.logicgate.colour.exception.ColourNotFoundException;
import com.logicgate.colour.model.Colour;

import java.util.List;

public interface ColourService {
    Colour addColour(Colour colour) throws ColourNotFoundException;
    Colour fetchColourById(Long id) throws ColourNotFoundException;
    Colour fetchColourByNameOrCode(String searchKey) throws ColourNotFoundException;
    List<Colour> fetchAllColours(Integer pageNumber);
    Colour updateColour(Colour colour,Long id) throws ColourNotFoundException;
    void deleteColour(Long id) throws ColourNotFoundException;
    void deleteAllColours();
}
