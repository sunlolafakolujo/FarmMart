package com.logicgate.colour.repository;


import com.logicgate.colour.model.Colour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColourRepository extends JpaRepository<Colour,Long>,ColourRepositoryCustom {
}
