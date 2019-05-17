package com.murk.contacts.filter.service;

import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactsService {

    @Override
    public ContactsResponse get(String regexp) {
        ContactsResponse contactsResponse = new ContactsResponse();

        contactsResponse.setContact(new Contact(1,"name 1"));
        contactsResponse.setContact(new Contact(2,"name 2"));
        contactsResponse.setContact(new Contact(3,"name 3"));

        return contactsResponse;
    }
}
