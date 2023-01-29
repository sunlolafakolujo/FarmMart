package com.logicgate.contact.service;

import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;
import com.logicgate.contact.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact addContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact fetchContactById(Long id) throws ContactNotFoundException {
        return contactRepository.findById(id).orElseThrow(()->new ContactNotFoundException("Contact ID "+id+" Not Found"));
    }

    @Override
    public List<Contact> fetchAllContacts(Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        return contactRepository.findAll(pageable).toList();
    }

    @Override
    public Contact updateContact(Contact contact, Long id) throws ContactNotFoundException {
        Contact savedContact=contactRepository.findById(id)
                .orElseThrow(()->new ContactNotFoundException("Contact ID "+id+" Not Found"));
        if (Objects.nonNull(contact.getHouseNumber()) && !"".equalsIgnoreCase(contact.getHouseNumber())){
            savedContact.setHouseNumber(contact.getHouseNumber());
        }if (Objects.nonNull(contact.getStreetName()) && !"".equalsIgnoreCase(contact.getStreetName())){
            savedContact.setStreetName(contact.getStreetName());
        }if (Objects.nonNull(contact.getLandmark()) && !"".equalsIgnoreCase(contact.getLandmark())){
            savedContact.setLandmark(contact.getLandmark());
        }if (Objects.nonNull(contact.getCity()) && !"".equalsIgnoreCase(contact.getCity())){
            savedContact.setCity(contact.getCity());
        }if (Objects.nonNull(contact.getStateProvince()) && !"".equalsIgnoreCase(contact.getStateProvince())){
            savedContact.setStateProvince(contact.getStateProvince());
        }if (Objects.nonNull(contact.getCountry()) && !"".equalsIgnoreCase(contact.getCountry())){
            savedContact.setCountry(contact.getCountry());
        }
        return contactRepository.save(savedContact);
    }

    @Override
    public void deleteContactById(Long id) throws ContactNotFoundException {
        if (contactRepository.existsById(id)){
            contactRepository.deleteById(id);
        }else {
            throw new ContactNotFoundException("Contact ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllContacts() {
        contactRepository.deleteAll();
    }
}
