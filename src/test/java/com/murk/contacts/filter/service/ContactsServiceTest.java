package com.murk.contacts.filter.service;


import com.murk.contacts.filter.model.ContactsResponse;
import com.murk.contacts.filter.process.Driver;
import com.murk.contacts.filter.util.DriverBuilder;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static mocks.model.MockModel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class ContactsServiceTest {


    @Mock
    private ThreadPoolTaskExecutor pool;

    @Mock
    private DriverBuilder driverBuilder;


    @InjectMocks
    @Autowired
    private ContactsService service;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoad()
    {
        assertThat(service).isNotNull();
        assertThat(driverBuilder).isNotNull();
        assertThat(pool).isNotNull();
    }

    @Test
    public void getSuccess() throws ExecutionException, InterruptedException {

        Driver driver = Mockito.mock(Driver.class);
        when(driver.getMappers()).thenReturn(5);
        when(driver.isInitMappers()).thenReturn(true);

        Future<ContactsResponse> responseFuture = (Future<ContactsResponse>) Mockito.mock(Future.class);
        when(responseFuture.get()).thenReturn(ALL_CONTACTS_RESPONSE);

        when(driverBuilder.build(Mockito.any())).thenReturn(driver);
        when(pool.submit(Mockito.any(Driver.class))).thenReturn(responseFuture);

        ContactsResponse response = service.get(GET_ALL_REGEXP);

        assertThat(response).isEqualToComparingFieldByField(ALL_CONTACTS_RESPONSE);


        verify(driverBuilder, times(1)).build(Mockito.any());

        verifyNoMoreInteractions(driverBuilder);
    }

    @Test(expected = InternalException.class)
    public void getFail() throws ExecutionException, InterruptedException {

        Driver driver = Mockito.mock(Driver.class);
        when(driver.getMappers()).thenReturn(5);
        when(driver.isInitMappers()).thenReturn(true);

        Future<ContactsResponse> responseFuture = (Future<ContactsResponse>) Mockito.mock(Future.class);
        when(responseFuture.get()).thenThrow(new InterruptedException());

        when(driverBuilder.build(Mockito.any())).thenReturn(driver);
        when(pool.submit(Mockito.any(Driver.class))).thenReturn(responseFuture);

        ContactsResponse response = service.get(GET_ALL_REGEXP);

    }
}
