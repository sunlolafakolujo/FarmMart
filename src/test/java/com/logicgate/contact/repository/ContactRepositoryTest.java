package com.logicgate.contact.repository;

import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
@Transactional
class ContactRepositoryTest {
    @Autowired
    private ContactRepository contactRepository;
    Contact contact;

    @BeforeEach
    void setUp() {
        contact=new Contact();
    }

    @Test
    void testThatYouCanSaveContact(){
        contact.setHouseNumber("13");
        contact.setStreetName("Ilogbo Road, Apapa Road, Ebute Metta");
        contact.setLandmark("AP Petrol Station");
        contact.setCity("Lagos Island");
        contact.setStateProvince("Lagos");
        contact.setCountry("Nigeria");
        log.info("Contact Repo before saving:{}",contact);
        assertDoesNotThrow(()->contactRepository.save(contact));
        assertEquals("Ilogbo Road, Apapa Road, Ebute Metta",contact.getStreetName());
        assertNotNull(contact.getId());
        log.info("Contact Repo after saving:{}",contact);
    }

    @Test
    void testThatYouCanFindContactById() throws ContactNotFoundException {
        Long id=3L;
        contact=contactRepository.findById(id)
                .orElseThrow(()->new ContactNotFoundException("Contact ID "+id+" Not Found"));
        assertNotNull(contact.getId());
        assertEquals(id,contact.getId());
        log.info("Contact ID "+id+": {}",contact);
    }

    @Test
    void testThatYouCanFindAllContacts(){
        List<Contact> contactList=contactRepository.findAll();
        contactList.forEach(System.out::println);
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteContactById() throws ContactNotFoundException {
        Long id=26L;
        if (contactRepository.existsById(id)){
            contactRepository.deleteById(id);
        }else {
            throw new ContactNotFoundException("Contact ID "+id+" Not Found");
        }
    }

    @Test
    @Rollback(value = false)
    void testThatYouCanDeleteAllContact(){
        contactRepository.deleteAll();
    }
}