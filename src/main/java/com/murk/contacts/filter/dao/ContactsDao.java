package com.murk.contacts.filter.dao;

import com.murk.contacts.filter.model.Contact;

import java.util.Set;

public interface ContactsDao
{
    Set<Contact> getBatch(long startId, int batchSize);
}
