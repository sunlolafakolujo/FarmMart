package com.logicgate.colour.repository;


import com.logicgate.colour.model.Colour;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ColourRepositoryCustom {
    @Query("FROM Colour c WHERE c.colourName=?1 OR c.colourCode=?2")
    Optional<Colour> findByColourNameOrCode(String colourName, String colourCode);
}
