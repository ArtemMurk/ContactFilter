package com.murk.contacts.filter.service;

import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class ContactServiceImpl implements ContactsService {
    private int mappers;
    @Autowired
    public ContactServiceImpl(@Qualifier("mapred")Properties mapredProps)
    {
        mappers = Integer.parseInt(mapredProps.getProperty("mapperSize"));
    }

    @Override
    public ContactsResponse get(String regexp) {
        ContactsResponse contactsResponse = new ContactsResponse();

        contactsResponse.setContact(new Contact(1,"name 1"));
        contactsResponse.setContact(new Contact(2,"name 2"));
        contactsResponse.setContact(new Contact(3,"name 3"));
        contactsResponse.setContact(new Contact(4,"Mapper size = "+mappers));

        return contactsResponse;
    }
}
