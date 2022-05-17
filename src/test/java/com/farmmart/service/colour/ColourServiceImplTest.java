package com.farmmart.service.colour;

import com.farmmart.data.model.colour.Colour;
import com.farmmart.data.model.colour.ColourNotFoundException;
import com.farmmart.data.repository.colour.ColourRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ColourServiceImplTest {

    @Mock
    private ColourRepository colourRepository;

    @InjectMocks
    private ColourService colourService=new ColourServiceImpl();

    Colour colour;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        colour=new Colour();
    }

    @Test
    void testThatYouCanMockSaveColourMethod() throws ColourNotFoundException {
        Mockito.when(colourRepository.save(colour)).thenReturn(colour);

        colourService.saveColour(colour);

        ArgumentCaptor<Colour> colourArgumentCaptor=ArgumentCaptor.forClass(Colour.class);

        Mockito.verify(colourRepository, Mockito.times(1)).save(colourArgumentCaptor.capture());

        Colour capturedColour=colourArgumentCaptor.getValue();

        assertEquals(capturedColour,colour);

    }

    @Test
    void testThatYouCanMockFindColourByIdMethod() throws ColourNotFoundException {

        Long id =3L;

        Mockito.when(colourRepository.findById(id)).thenReturn(Optional.of(colour));

        colourService.findColourById(id);

        Mockito.verify(colourRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindColourByNameMethod() throws ColourNotFoundException {
        String name ="Blue";

        Mockito.when(colourRepository.findColourByName(name)).thenReturn(colour);

        colourService.findColourByName(name);

        Mockito.verify(colourRepository, Mockito.times(1)).findColourByName(name);
    }

    @Test
    void testThatYouCanMockFindAllColoursMethod() {
        List<Colour> colours=new ArrayList<>();

        Mockito.when(colourRepository.findAll()).thenReturn(colours);

        colourService.findAllColours(10);

        Mockito.verify(colourRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testThatYiuCanMockUpdateColourMethod() throws ColourNotFoundException {

        Long id =2L;

        Mockito.when(colourRepository.findById(id)).thenReturn(Optional.of(colour));

        colourService.updateColour(colour, id);

        Mockito.verify(colourRepository, Mockito.times(1)).save(colour);
    }

    @Test
    void testThatYouCanMockDeleteColourByIdMethod() throws ColourNotFoundException {

        Long id =7L;

        Mockito.doNothing().when(colourRepository).deleteById(id);

        colourService.deleteColourById(id);

        Mockito.verify(colourRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllColoursMethod() {

        Mockito.doNothing().when(colourRepository).deleteAll();

        colourService.deleteAllColours();

        Mockito.verify(colourRepository, Mockito.times(1)).deleteAll();
    }
}