package mocks.model;

import com.murk.contacts.filter.model.Contact;
import com.murk.contacts.filter.model.ContactsResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MockModel {
    public static  String GET_ALL_REGEXP;

    static {
        try {
            GET_ALL_REGEXP = URLEncoder.encode("[0-9A-Za-z]*", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static final Contact CONTACT_1 = new Contact(1,"1name");
    public static final Contact CONTACT_2 = new Contact(1,"2name");
    public static final Contact CONTACT_3 = new Contact(1,"3name");
    public static final Contact CONTACT_4 = new Contact(1,"4name");
    public static final Contact CONTACT_5 = new Contact(1,"5name");
    public static final Contact CONTACT_6 = new Contact(1,"6name");

    public static final ContactsResponse ALL_CONTACTS_RESPONSE = new ContactsResponse();

    static
    {
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_1);
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_2);
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_3);
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_4);
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_5);
        ALL_CONTACTS_RESPONSE.setContact(CONTACT_6);
    }
}
