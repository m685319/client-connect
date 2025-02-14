package com.example.clients.controller;

import com.example.clients.dto.ContactDTO;
import com.example.clients.exception.EntityNotFoundException;
import com.example.clients.exception.GlobalExceptionHandler;
import com.example.clients.service.ContactService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    private static final Long CONTACT_ID = 1L;

    private static MockMvc mockMvc;

    private static ContactService contactService;

    private static ContactDTO contactDTO;

    @BeforeAll
    static void beforeAll() {
        contactService = mock(ContactService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ContactController(contactService))
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();

        contactDTO = new ContactDTO();
        contactDTO.setId(CONTACT_ID);
        contactDTO.setPhone("+123456789");
        contactDTO.setEmail("test@example.com");
    }

    @AfterEach
    void tearDown() {
        reset(contactService);
    }

    @Test
    void testGetAllContacts() throws Exception {
        var contacts = Arrays.asList(contactDTO);
        when(contactService.getAllContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].phone").value("+123456789"));

        verify(contactService, times(1)).getAllContacts();
    }

    @Test
    void testGetContactById() throws Exception {
        when(contactService.getContactById(CONTACT_ID)).thenReturn(contactDTO);

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+123456789"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(contactService, times(1)).getContactById(CONTACT_ID);
    }

    @Test
    void testGetContactById_NotFound() throws Exception {
        when(contactService.getContactById(CONTACT_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).getContactById(CONTACT_ID);
    }

    @Test
    void testCreateContact() throws Exception {
        when(contactService.saveContact(any(ContactDTO.class))).thenReturn(contactDTO);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+123456789\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone").value("+123456789"));

        verify(contactService, times(1)).saveContact(any(ContactDTO.class));
    }

    @Test
    void testUpdateContact() throws Exception {
        when(contactService.getContactById(CONTACT_ID)).thenReturn(contactDTO);
        when(contactService.updateContact(eq(CONTACT_ID), any(ContactDTO.class))).thenReturn(contactDTO);

        mockMvc.perform(put("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+987654321\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(contactService, times(1)).updateContact(eq(CONTACT_ID) ,any(ContactDTO.class));
    }

    @Test
    void testDeleteContact() throws Exception {
        mockMvc.perform(delete("/contacts/1"))
                .andExpect(status().isNoContent());

        verify(contactService, times(1)).deleteContact(CONTACT_ID);
    }

}
