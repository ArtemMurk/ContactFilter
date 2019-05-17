package com.murk.contacts.filter.service;

import com.murk.contacts.filter.model.ContactsResponse;
import com.murk.contacts.filter.process.ContactDriverProcess;
import exception.OverloadException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ContactServiceImpl implements ContactsService {
    private int maxMappers;
    private int minMappers;
    private int batch;
    private int totalSlots;

    private ReentrantLock locker = new ReentrantLock();

    private ThreadPoolTaskExecutor slotsPool;


    @Autowired
    public ContactServiceImpl(@Qualifier("mapred")Properties mapredProps, ThreadPoolTaskExecutor slotsPool)
    {
        maxMappers = Integer.parseInt(mapredProps.getProperty("maxMappers"));
        minMappers = Integer.parseInt(mapredProps.getProperty("minMappers"));
        batch = Integer.parseInt(mapredProps.getProperty("batch"));
        totalSlots = Integer.parseInt(mapredProps.getProperty("slots"));

        this.slotsPool = slotsPool;
    }

    @Override
    public ContactsResponse get(String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        ContactsResponse response;

        locker.lock();

        Future<ContactsResponse> responseFuture;
        try {
           ContactDriverProcess driver = getNewDriver(pattern);

           responseFuture = slotsPool.submit(driver);
           log.info("Start process for get contacts. Regexp ={}, mappers = {}", regexp, driver.getMappers());
           while (!driver.isInitMappers()) {
               try {
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


    private ContactDriverProcess getNewDriver(Pattern pattern)
    {
        ContactDriverProcess contactDriverProcess;
        int activeSlots = slotsPool.getThreadPoolExecutor().getActiveCount();
        int avaliableSlots = totalSlots - activeSlots;

        if (avaliableSlots>minMappers+1)
        {
            int mappers;
            if (avaliableSlots<=maxMappers)
            {
                mappers = avaliableSlots - 1;
            } else
                {
                    mappers = maxMappers;
                }

            contactDriverProcess = new ContactDriverProcess(batch, mappers,pattern,slotsPool);
        } else
            {
                throw new OverloadException("Service temporary overload, avaliable slots = "+avaliableSlots+", but min slots need = "+minMappers+1);
            }

        return contactDriverProcess;
    }
}
