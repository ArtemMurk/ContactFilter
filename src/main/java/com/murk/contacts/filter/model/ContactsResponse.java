package com.murk.contacts.filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ContactsResponse {

    @JsonProperty("contacts")
    Set<Contact> contacts = ConcurrentHashMap.newKeySet();

    public void setContact(Contact contact)
    {
        contacts.add(contact);
    }
}
