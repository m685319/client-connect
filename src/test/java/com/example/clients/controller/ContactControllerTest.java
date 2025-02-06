package com.example.clients.controller;

import com.example.clients.model.Contact;
import com.example.clients.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ContactController.class)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact();
        contact.setId(1L);
        contact.setPhone("+123456789");
        contact.setEmail("test@example.com");
    }

    @Test
    void testGetAllContacts() throws Exception {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactService.getAllContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].phone").value("+123456789"));

        verify(contactService, times(1)).getAllContacts();
    }

    @Test
    void testGetContactById() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+123456789"));

        verify(contactService, times(1)).getContactById(1L);
    }

    @Test
    void testGetContactById_NotFound() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/contacts/1"))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).getContactById(1L);
    }

    @Test
    void testCreateContact() throws Exception {
        when(contactService.saveContact(any(Contact.class))).thenReturn(contact);

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+123456789\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.phone").value("+123456789"));

        verify(contactService, times(1)).saveContact(any(Contact.class));
    }

    @Test
    void testUpdateContact() throws Exception {
        when(contactService.getContactById(1L)).thenReturn(Optional.of(contact));
        when(contactService.saveContact(any(Contact.class))).thenReturn(contact);

        mockMvc.perform(put("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phone\":\"+987654321\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+987654321"));

        verify(contactService, times(1)).getContactById(1L);
        verify(contactService, times(1)).saveContact(any(Contact.class));
    }

    @Test
    void testDeleteContact() throws Exception {
        doNothing().when(contactService).deleteContact(1L);

        mockMvc.perform(delete("/contacts/1"))
                .andExpect(status().isNoContent());

        verify(contactService, times(1)).deleteContact(1L);
    }

}