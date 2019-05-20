package com.murk.contacts.filter.process;

import com.murk.contacts.filter.dao.ContactsDao;
import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Slf4j
public class ContactMapperProcess implements Runnable{
    private Pattern pattern;
    private ContactsResponse contactsResponse;
    private int mapperNumber;
    private  int totalMappers;
    private  int batchSize;
    private AtomicInteger finishMapperCounter;
    private ContactsDao dao;


    public ContactMapperProcess(ContactsDao dao,
                                Pattern pattern,
                                ContactsResponse contactsResponse,
                                int mapperNumber,
                                int totalMappers,
                                int batchSize,
                                AtomicInteger finishMapperCounter)
    {
        this.pattern = pattern;
        this.contactsResponse = contactsResponse;
        this.mapperNumber = mapperNumber;
        this.totalMappers = totalMappers;
        this.finishMapperCounter = finishMapperCounter;
        this.dao =dao;
        this.batchSize =batchSize;
    }

    @Override
    public void run() {
        int batchNum = 0;
        Set<Contact> batchContacts;
        try {
            do
                {
                    long startId = getStartId(batchNum);

                    batchContacts = dao.getBatch(startId, batchSize);
                    if (batchContacts != null && batchContacts.size()>0)
                    {
                        filterContatcs(batchContacts);
                    }

                    batchNum++;
                } while (batchContacts == null || batchContacts.size()>0);
            log.info("Mapper №{} for regexp = \"{}\" is finish successful",mapperNumber,pattern.pattern());
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            finish();
        }

    }

    private long getStartId(int batchNum) {
        return (mapperNumber*batchSize)+(batchNum*totalMappers*batchSize);
    }

    private void filterContatcs(Set<Contact> batchContacts) {
        Set<Contact> filteredContact = new HashSet<>();
        batchContacts.forEach(contact ->
        {
            if (pattern.matcher(contact.getName()).matches())
            {
                contactsResponse.setContact(contact);
            }
        });
    }


    private void finish() {
        finishMapperCounter.incrementAndGet();
    }
}
