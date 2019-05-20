package com.murk.contacts.filter.util;

import com.murk.contacts.filter.dao.ContactsDao;
import com.murk.contacts.filter.exception.OverloadException;
import com.murk.contacts.filter.process.Driver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

import static mocks.model.MockModel.GET_ALL_REGEXP;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class DriverBuilderTest {

    @Mock
    private ThreadPoolTaskExecutor pool;

    @Mock
    private ContactsDao dao;

    @InjectMocks
    @Autowired
    private DriverBuilder builder;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void buildSuccess()
    {
        when(pool.getActiveCount()).thenReturn(0);
        Driver response = builder.build(Pattern.compile(GET_ALL_REGEXP));
        assertEquals(response.getPattern().toString(),Pattern.compile(GET_ALL_REGEXP).toString());

    }

    @Test(expected = OverloadException.class)
    public void overload()
    {
        when(pool.getActiveCount()).thenReturn(Integer.MAX_VALUE);
        builder.build(Pattern.compile(GET_ALL_REGEXP));

    }
}