package com.logicgate.contact.service;

import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;
import com.logicgate.contact.repository.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ContactServiceImplTest {
    @Mock
    private ContactRepository contactRepository;
    @InjectMocks
    private ContactService contactService=new ContactServiceImpl();
    Contact contact;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        contact=new Contact();
    }

    @Test
    void testThatYouCanMockAddContactMethod(){
        when(contactRepository.save(contact)).thenReturn(contact);
        contactService.addContact(contact);
        ArgumentCaptor<Contact> contactArgumentCaptor=ArgumentCaptor.forClass(Contact.class);
        Mockito.verify(contactRepository,Mockito.times(1)).save(contactArgumentCaptor.capture());
        Contact capturedContact=contactArgumentCaptor.getValue();
        assertEquals(capturedContact,contact);
    }

    @Test
    void testThatYouCanMockFindContactByIdMethod() throws ContactNotFoundException {
        Long id=20L;
        when(contactRepository.findById(id)).thenReturn(Optional.of(contact));
        contactService.fetchContactById(id);
        verify(contactRepository,times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindAllContacts(){
        Integer pageNumber=0;
        Pageable pageable= PageRequest.of(pageNumber,10);
        List<Contact> contacts=new ArrayList<>();
        Page<Contact> contactPage=new PageImpl<>(contacts);
        when(contactRepository.findAll(pageable)).thenReturn(contactPage);
        contactService.fetchAllContacts(pageNumber);
        verify(contactRepository,times(1)).findAll(pageable);
    }

    @Test
    void testThatYouCanDeleteContactById() throws ContactNotFoundException {
        Long id=14L;
        doNothing().when(contactRepository).deleteById(id);
        contactService.deleteContactById(id);
        verify(contactRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanDeleteAllContacts(){
        doNothing().when(contactRepository).deleteAll();
        contactService.deleteAllContacts();
        verify(contactRepository,times(1)).deleteAll();
    }
}