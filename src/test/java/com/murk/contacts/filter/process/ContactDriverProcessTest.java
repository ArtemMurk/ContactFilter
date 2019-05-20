package com.murk.contacts.filter.process;

import com.murk.contacts.filter.dao.ContactsDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Pattern;

import static mocks.model.MockModel.GET_ALL_REGEXP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class ContactDriverProcessTest
{
    @Mock
    private ThreadPoolTaskExecutor pool;

    @Mock
    private ContactsDao dao;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void runSuccess() throws Exception {
        int mappers = 0;
        ContactDriverProcess driverProcess = new ContactDriverProcess(dao, mappers, Pattern.compile(GET_ALL_REGEXP), pool, 5);

        driverProcess.call();
        verify(pool, times(mappers)).execute(Mockito.any(ContactMapperProcess.class));

        assertThat(driverProcess.isInitMappers()).isTrue();

        verifyNoMoreInteractions(pool);
        verifyNoMoreInteractions(dao);
    }
}
