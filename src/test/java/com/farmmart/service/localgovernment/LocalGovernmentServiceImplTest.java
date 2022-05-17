package com.farmmart.service.localgovernment;

import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.data.repository.localgovernment.LocalGovernmentRepository;
import com.farmmart.data.repository.state.StateRepository;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class LocalGovernmentServiceImplTest {

    @Mock
    private LocalGovernmentRepository localGovernmentRepository;

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private LocalGovernmentService localGovernmentService=new LocalGovernmentServiceImpl();

    LocalGovernment localGovernment;

    States state;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        localGovernment=new LocalGovernment();
        state=new States();
    }

    @Test
    void testThatYouCanMockSaveLocalGovernmentMethod() throws LocalGovernmentNotFoundException {
        Mockito.when(localGovernmentRepository.save(localGovernment)).thenReturn(localGovernment);

        localGovernmentService.saveLocalGovernment(localGovernment);

        ArgumentCaptor<LocalGovernment> localGovernmentArgumentCaptor=ArgumentCaptor.forClass(LocalGovernment.class);

        Mockito.verify(localGovernmentRepository,Mockito.times(1)).save(localGovernmentArgumentCaptor.capture());

        LocalGovernment captureLocalGovernment=localGovernmentArgumentCaptor.getValue();

        assertEquals(captureLocalGovernment,localGovernment);
    }

    @Test
    void testThatYouCanMockFindLocalGovernmentByIdMethod() throws LocalGovernmentNotFoundException {
        Long id=4L;
        Mockito.when(localGovernmentRepository.findById(id)).thenReturn(Optional.of(localGovernment));

        localGovernmentService.findLocalGovernmentById(id);

        Mockito.verify(localGovernmentRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindLocalGovernmentByNameMethod() throws LocalGovernmentNotFoundException {
        String localGovtName="Agege";

        Mockito.when(localGovernmentRepository.findByLocalGovernmentName(localGovtName)).thenReturn(localGovernment);

        localGovernmentService.findLocalGovernmentByName(localGovtName);

        Mockito.verify(localGovernmentRepository,Mockito.times(1))
                .findByLocalGovernmentName(localGovtName);
    }

    @Test
    void testThatYouCanMockFindAllLocalGovernmentMethod() throws LocalGovernmentNotFoundException {
        List<LocalGovernment> localGovernmentList=new ArrayList<>();

        Mockito.when(localGovernmentRepository.findAll()).thenReturn(localGovernmentList);

        localGovernmentService.findAllLocalGovernment(10);

        Mockito.verify(localGovernmentRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockFindLocalGovernmentByStateMethod() {
        String stateName="Lagos";
        state=stateRepository.findByStateName(stateName);

        List<LocalGovernment> localGovernments=new ArrayList<>();

        Mockito.when(localGovernmentRepository.findLocalGovernmentByState(state)).thenReturn(localGovernments);

        localGovernmentService.findLocalGovernmentByState(state,stateName);

        Mockito.verify(localGovernmentRepository,Mockito.times(1)).findLocalGovernmentByState(state);
    }

    @Test
    void updateLocalGovernment() throws LocalGovernmentNotFoundException {
        Long id=5L;
        Mockito.when(localGovernmentRepository.findById(id)).thenReturn(Optional.of(localGovernment));

        localGovernmentService.updateLocalGovernment(localGovernment,id);

        Mockito.verify(localGovernmentRepository,Mockito.times(1)).save(localGovernment);
    }

    @Test
    void testThatYouCanMockDeleteLocalGovernmentByIdMethod() throws LocalGovernmentNotFoundException {
        Long id=4L;
        doNothing().when(localGovernmentRepository).deleteById(id);

        localGovernmentService.deleteLocalGovernmentById(id);

        verify(localGovernmentRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllLocalGovernmentMethod() {
        doNothing().when(localGovernmentRepository).deleteAll();

        localGovernmentService.deleteAllLocalGovernment();

        verify(localGovernmentRepository,times(1)).deleteAll();
    }
}