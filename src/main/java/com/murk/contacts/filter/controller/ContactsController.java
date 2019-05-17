package com.murk.contacts.filter.controller;


import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;
import com.murk.contacts.filter.service.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/hello")
public class ContactsController {

    private ContactsService service;

    @Autowired
    public ContactsController(ContactsService service) {
        this.service = service;
    }



    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public ResponseEntity<ContactsResponse> ping(@RequestParam("nameFilter") String nameFilter)
    {
        log.info("get contacts for regexp = \"{}\"",nameFilter);

        ContactsResponse contactsResponse = new ContactsResponse();

        contactsResponse.setContact(new Contact(1,"name 1"));
        contactsResponse.setContact(new Contact(2,"name 2"));
        contactsResponse.setContact(new Contact(3,"name 3"));

        return new ResponseEntity<>(contactsResponse,  HttpStatus.OK);
    }
}
