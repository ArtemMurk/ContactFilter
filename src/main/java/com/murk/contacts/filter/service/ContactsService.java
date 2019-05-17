package com.murk.contacts.filter.service;

import com.murk.contacts.filter.model.ContactsResponse;

public interface ContactsService {
    ContactsResponse get(String regexp);
}
