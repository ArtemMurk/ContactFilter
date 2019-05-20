package com.murk.contacts.filter.process;

import com.murk.contacts.filter.dao.ContactsDao;
import com.murk.contacts.filter.model.ContactsResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ContactDriverProcess implements Driver {

    private volatile boolean isInitMappers;
    private int mappers;
    private int batchSize;
    private Pattern pattern;
    private ThreadPoolTaskExecutor slotsPool;
    private ContactsDao dao;

    public ContactDriverProcess(ContactsDao dao,
                                int mappers,
                                Pattern pattern,
                                ThreadPoolTaskExecutor slotsPool,
                                int batchSize)
    {
        this.mappers = mappers;
        this.pattern = pattern;
        this.slotsPool= slotsPool;
        this.dao = dao;
        this.batchSize = batchSize;
    }

    @Override
    public ContactsResponse call() throws Exception {
        ContactsResponse contactsResponse = new ContactsResponse();

        AtomicInteger finistMapper = new AtomicInteger(0);
        initMappers(contactsResponse,finistMapper);

        while (finistMapper.get()!=mappers)
        {
            Thread.sleep(100);
        }

        return contactsResponse;
    }

    private void initMappers(ContactsResponse contactsResponse, AtomicInteger finistMapper) {
        for (int i = 0; i < mappers; i++) {
            ContactMapperProcess mapper= new ContactMapperProcess(dao,pattern,contactsResponse,i,mappers,batchSize,finistMapper);
            slotsPool.execute(mapper);
        }

        isInitMappers = true;
    }

    public int getMappers() {
        return mappers;
    }

    public boolean isInitMappers() {
        return isInitMappers;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
