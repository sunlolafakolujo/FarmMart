package com.logicgate.contact.controller;

import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;
import com.logicgate.contact.model.ContactDto;
import com.logicgate.contact.model.NewContact;
import com.logicgate.contact.model.UpdateContact;
import com.logicgate.contact.service.ContactService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
public class ContactController {
    private final ContactService contactService;
    private final ModelMapper modelMapper;

    @PostMapping("/addContact")
    public ResponseEntity<NewContact> addContact(@RequestBody NewContact newContact){
        Contact contact=modelMapper.map(newContact, Contact.class);
        Contact post=contactService.addContact(contact);
        NewContact posted=modelMapper.map(post, NewContact.class);
        return new ResponseEntity<>(posted, HttpStatus.CREATED);
    }

    @GetMapping("/findContactById")
    public ResponseEntity<ContactDto> getContactById(@RequestParam("id") Long id) throws ContactNotFoundException {
        Contact contact=contactService.fetchContactById(id);
        return new ResponseEntity<>(convertContactToDto(contact),HttpStatus.OK);
    }

    @GetMapping("/findAllContacts")
    public ResponseEntity<List<ContactDto>> getAllContacts(@RequestParam(defaultValue = "0") Integer pageNumber){
        return new ResponseEntity<>(contactService.fetchAllContacts(pageNumber)
                .stream()
                .map(this::convertContactToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/updateContact")
    public ResponseEntity<UpdateContact> updateContact(@RequestBody UpdateContact updateContact,
                                                       @RequestParam("id") Long id) throws ContactNotFoundException {
        Contact contact=modelMapper.map(updateContact, Contact.class);
        Contact post=contactService.updateContact(contact, id);
        UpdateContact posted=modelMapper.map(post, UpdateContact.class);
        return new ResponseEntity<>(posted, HttpStatus.OK);
    }

    @DeleteMapping("/deleteContact")
    public ResponseEntity<?> deleteContactById(@RequestParam("id") Long id) throws ContactNotFoundException {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllContacts")
    public ResponseEntity<?> deleteAllContacts(){
        contactService.deleteAllContacts();
        return ResponseEntity.noContent().build();
    }


    private ContactDto convertContactToDto(Contact contact){
        ContactDto contactDto=new ContactDto();
        contactDto.setId(contact.getId());
        contactDto.setHouseNumber(contact.getHouseNumber());
        contactDto.setStreetName(contact.getStreetName());
        contactDto.setCity(contact.getCity());
        contactDto.setLandmark(contact.getLandmark());
        contactDto.setStateProvince(contact.getStateProvince());
        contactDto.setCountry(contact.getCountry());
        return contactDto;
    }
}
