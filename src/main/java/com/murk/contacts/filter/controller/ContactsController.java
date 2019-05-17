package com.murk.contacts.filter.controller;


import com.murk.contacts.filter.model.ContactsResponse;
import com.murk.contacts.filter.service.ContactsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.regex.PatternSyntaxException;

@RestController
@Slf4j
@RequestMapping("/hello")
public class ContactsController {

    private ContactsService service;

    @Autowired
    public ContactsController(ContactsService service) {
        this.service = service;
    }



    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public ResponseEntity<ContactsResponse> ping(@RequestParam("nameFilter") String nameFilter)
    {
        log.info("get contacts for regexp = \"{}\"",nameFilter);

        ContactsResponse contacts = service.get(nameFilter);

        return new ResponseEntity<>(contacts,  HttpStatus.OK);
    }


    @ExceptionHandler(value= { PatternSyntaxException.class})
    private ResponseEntity<Object> notValidRegexp(RuntimeException ex, WebRequest request)
    {
        String badRegexp = request.getParameter("nameFilter");
        log.error("Catch not valid regexp = \"{}\"",badRegexp);
        return new ResponseEntity<>(
                "Not valid regexp = "+badRegexp, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
