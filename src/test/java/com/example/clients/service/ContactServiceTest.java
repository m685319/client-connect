package com.example.clients.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.clients.model.Contact;
import com.example.clients.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
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
    void testGetAllContacts() {
        List<Contact> contacts = Arrays.asList(contact);
        when(contactRepository.findAll()).thenReturn(contacts);

        List<Contact> result = contactService.getAllContacts();

        assertEquals(1, result.size());
        assertEquals("+123456789", result.get(0).getPhone());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void testGetContactById() {
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Optional<Contact> result = contactService.getContactById(1L);

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveContact() {
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = contactService.saveContact(contact);

        assertNotNull(result);
        assertEquals("+123456789", result.getPhone());
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void testDeleteContact() {
        doNothing().when(contactRepository).deleteById(1L);

        contactService.deleteContact(1L);

        verify(contactRepository, times(1)).deleteById(1L);
    }

}