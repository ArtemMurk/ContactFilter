package com.murk.contacts.filter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Contact {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
