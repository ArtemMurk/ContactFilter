package com.murk.contacts.filter.process;

import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ContactMapperProcess implements Runnable{
    private Pattern pattern;
    private ContactsResponse contactsResponse;
    private int batch;
    private int number;
    private  int totalMappers;
    private AtomicInteger finishMapper;


    public ContactMapperProcess(Pattern pattern, ContactsResponse contactsResponse, int batch, int number, int totalMappers, AtomicInteger finishMapper) {
        this.pattern = pattern;
        this.contactsResponse = contactsResponse;
        this.batch = batch;
        this.number = number;
        this.totalMappers = totalMappers;
        this.finishMapper = finishMapper;
    }

    @Override
    public void run() {
        contactsResponse.setContact(new Contact(number,"yahoo hello world"));
        finish();
    }

    private void finish() {
        finishMapper.incrementAndGet();
    }
}
