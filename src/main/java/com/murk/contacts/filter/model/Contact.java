package com.murk.contacts.filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Contact {

    @JsonProperty("id")
    long id;

    @JsonProperty("name")
    String name;

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
