package com.logicgate.contact.service;

import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;

import java.util.List;

public interface ContactService {
    Contact addContact(Contact contact);
    Contact fetchContactById(Long id) throws ContactNotFoundException, ContactNotFoundException;
    List<Contact> fetchAllContacts(Integer pageNumber);
    Contact updateContact(Contact contact, Long id) throws ContactNotFoundException;
    void deleteContactById(Long id) throws ContactNotFoundException;
    void deleteAllContacts();
}
