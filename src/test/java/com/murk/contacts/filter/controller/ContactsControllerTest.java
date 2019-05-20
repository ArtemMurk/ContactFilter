package com.murk.contacts.filter.controller;

import com.murk.contacts.filter.exception.OverloadException;
import com.murk.contacts.filter.service.ContactsService;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.regex.PatternSyntaxException;

import static mocks.model.MockModel.*;
import static mocks.request.MockRequests.GET_ALL_CONTACT_REQUEST;
import static mocks.request.MockRequests.NOT_VALID_REGEXP;
import static mocks.request.MockRequests.PARSE_EXCEPTION_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactsControllerTest {

    private MockMvc mockMvc;

    private ContactsService service;

    private ContactsController controller;

    @Before
    public void setUp(){
        service = mock(ContactsService.class);
        controller = new ContactsController (service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoad()
    {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
        assertThat(controller).isNotNull();
    }

    @Test
    public void filterSuccess() throws Exception {
        when(service.get(GET_ALL_REGEXP)).thenReturn(ALL_CONTACTS_RESPONSE);


        mockMvc.perform(get(GET_ALL_CONTACT_REQUEST))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacts.length()", is(ALL_CONTACTS_RESPONSE.getContacts().size())));

        verify(service, times(1)).get(GET_ALL_REGEXP);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void notValidRegexp() throws Exception {
        String desc = "description";
        String regex = NOT_VALID_REGEXP;
        int index = 1;

        when(service.get(NOT_VALID_REGEXP)).thenThrow(new PatternSyntaxException(desc,regex,index));


        mockMvc.perform(get(PARSE_EXCEPTION_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$",is("Not valid regexp = "+NOT_VALID_REGEXP)));
    }

    @org.junit.Test
    public void serviceTemporalyOverload() throws Exception {
        String overloadMessage = "overloadMessage";

        when(service.get(GET_ALL_REGEXP)).thenThrow(new OverloadException(overloadMessage));


        mockMvc.perform(get(GET_ALL_CONTACT_REQUEST))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$",is(overloadMessage)));

    }

    @Test
    public void internalException() throws Exception {
        String internalErrorMessage = "overloadMessage";

        when(service.get(GET_ALL_REGEXP)).thenThrow(new InternalException(internalErrorMessage));


        mockMvc.perform(get(GET_ALL_CONTACT_REQUEST))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$",is(internalErrorMessage)));

    }
}