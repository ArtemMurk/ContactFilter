package com.murk.contacts.filter.process;

import com.murk.contacts.filter.model.ContactsResponse;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public interface Driver extends Callable<ContactsResponse>
{
    int getMappers();
    boolean isInitMappers();
    Pattern getPattern();
}
