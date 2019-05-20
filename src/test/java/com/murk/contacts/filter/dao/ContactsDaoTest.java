package com.murk.contacts.filter.dao;

import com.murk.contacts.filter.model.Contact;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static mocks.model.MockModel.*;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = "file:src/test/resources/test-dao-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ContactsDaoTest {


    @Autowired
    private ContactsDao dao;

    @Autowired
    private BasicDataSource ds;

    @Before
    public void initDb()
    {
        try(Connection connection = ds.getConnection())
        {
            InputStream createTableStream  = new FileInputStream("src/main/resources/db/init_db.sql");
            DBHelper.importSQL(connection,createTableStream);

            InputStream insertMocksStream  = new FileInputStream("src/test/resources/db/insert_mock.sql");
            DBHelper.importSQL(connection,insertMocksStream);


        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getBatchSuccess()
    {
        Set<Contact> contactSet =dao.getBatch(0,2);
        assertThat(contactSet).contains(CONTACT_1,CONTACT_2 );

    }

    @Test
    public void getAllSuccess()
    {
        Set<Contact> contactSet =dao.getBatch(0,Integer.MAX_VALUE);
        assertThat(contactSet).isEqualTo(ALL_CONTACTS_RESPONSE.getContacts());
    }

    @Test
    public void getBatchNotFound()
    {
        Set<Contact> contactSet =dao.getBatch(Integer.MAX_VALUE,0);
        assertThat(contactSet).isEmpty();
    }
}