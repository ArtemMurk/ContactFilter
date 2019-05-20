package com.murk.contacts.filter.dao;

import com.murk.contacts.filter.model.Contact;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ContactsDaoImpl implements ContactsDao {
    private BasicDataSource ds;

    @Autowired
    public ContactsDaoImpl(BasicDataSource ds) {
        this.ds = ds;
    }

    @Override
    public Set<Contact> getBatch(long startId, int batchSize) {
        Set<Contact> contacts = new HashSet<>();
        try (Connection connection = ds.getConnection()) {
            try (PreparedStatement getContactsStatement = connection.prepareStatement("SELECT * FROM contacts OFFSET ? LIMIT ? ")){

                getContactsStatement.setLong(1,startId);
                getContactsStatement.setLong(2,batchSize);

                try (ResultSet rs = getContactsStatement.executeQuery()){
                    while (rs.next())
                    {
                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        Contact contact = new Contact(id,name);
                        contacts.add(contact);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
