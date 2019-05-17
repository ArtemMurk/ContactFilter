package com.murk.contacts.filter.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/hello")
public class ContactsController {

    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
    public ResponseEntity<String> ping(@RequestParam("nameFilter") String nameFilter)
    {
        log.info("get contacts for regexp = \"{}\"",nameFilter);
        return new ResponseEntity<>("Hello world, your regexp= "+nameFilter,  HttpStatus.OK);
    }
}
