package com.murk.contacts.filter.util;

import com.murk.contacts.filter.dao.ContactsDao;
import com.murk.contacts.filter.exception.OverloadException;
import com.murk.contacts.filter.process.ContactDriverProcess;
import com.murk.contacts.filter.process.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.regex.Pattern;

@Component
public class DriverBuilder
{
    private int maxMappers;
    private int minMappers;
    private int totalSlots;
    private int batchSize;
    private ContactsDao dao;

    private ThreadPoolTaskExecutor slotsPool;


    @Autowired
    public DriverBuilder(@Qualifier("mapred") Properties mapredProps, ThreadPoolTaskExecutor slotsPool, ContactsDao dao)
    {
        maxMappers = Integer.parseInt(mapredProps.getProperty("maxMappers"));
        minMappers = Integer.parseInt(mapredProps.getProperty("minMappers"));
        totalSlots = Integer.parseInt(mapredProps.getProperty("slots"));
        batchSize = Integer.parseInt(mapredProps.getProperty("batch"));


        this.dao = dao;

        this.slotsPool = slotsPool;
    }

    public Driver build(Pattern pattern)
    {
        ContactDriverProcess contactDriverProcess;
        int activeSlots = slotsPool.getActiveCount();
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

            contactDriverProcess = new ContactDriverProcess(dao,mappers,pattern,slotsPool,batchSize);
        } else
        {
            throw new OverloadException("Service temporary overload, avaliable slots = "+avaliableSlots+", but min slots need = "+minMappers+1);
        }

        return contactDriverProcess;
    }
}
