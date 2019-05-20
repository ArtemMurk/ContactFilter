package com.murk.contacts.filter.service;

import com.murk.contacts.filter.model.ContactsResponse;
import com.murk.contacts.filter.process.Driver;
import com.murk.contacts.filter.util.DriverBuilder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ContactServiceImpl implements ContactsService {

    private ReentrantLock locker = new ReentrantLock();
    private DriverBuilder driverBuilder;

    private ThreadPoolTaskExecutor slotsPool;


    @Autowired
    public ContactServiceImpl(ThreadPoolTaskExecutor slotsPool, DriverBuilder driverBuilder)
    {

        this.driverBuilder = driverBuilder;
        this.slotsPool = slotsPool;
    }

    @Override
    public ContactsResponse get(String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        ContactsResponse response;

        locker.lock();

        Future<ContactsResponse> responseFuture;
        try {
           Driver driver = driverBuilder.build(pattern);

           responseFuture = slotsPool.submit(driver);
           log.info("Start process for get contacts. Regexp ={}, mappers = {}", regexp, driver.getMappers());
           while (!driver.isInitMappers()) {
               try
               {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   log.error(e.getMessage());
               }
           }

       } finally {
           locker.unlock();
       }

        try {
            response = responseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new InternalException(e.getMessage());
        }

        return response;
    }


}
