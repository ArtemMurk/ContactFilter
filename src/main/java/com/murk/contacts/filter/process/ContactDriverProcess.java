package com.murk.contacts.filter.process;

import com.murk.contacts.filter.model.ContactsResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class ContactDriverProcess implements Callable<ContactsResponse> {

    private volatile boolean isInitMappers;
    private int batch;
    private int mappers;
    private Pattern pattern;
    private ThreadPoolTaskExecutor slotsPool;

    public ContactDriverProcess(int batch, int mappers, Pattern pattern, ThreadPoolTaskExecutor slotsPool) {
        this.batch = batch;
        this.mappers = mappers;
        this.pattern = pattern;
        this.slotsPool= slotsPool;
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
            ContactMapperProcess mapper= new ContactMapperProcess(pattern,contactsResponse,batch,i+1,mappers,finistMapper);
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
}
