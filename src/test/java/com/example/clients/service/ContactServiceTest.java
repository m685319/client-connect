package com.example.clients.service;

import com.example.clients.dto.ContactDTO;
import com.example.clients.mapper.ContactMapper;
import com.example.clients.model.Contact;
import com.example.clients.repository.ContactRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactService contactService;

    private static Contact contact;
    private static ContactDTO contactDTO;

    @BeforeAll
    static void beforeAll() {
        contact = new Contact();
        contact.setId(1L);
        contact.setPhone("+123456789");
        contact.setEmail("test@example.com");

        contactDTO = new ContactDTO();
        contactDTO.setId(1L);
        contactDTO.setPhone("+123456789");
        contactDTO.setEmail("test@example.com");
    }

    @Test
    void testGetAllContacts() {
        // given
        var contacts = List.of(contact);
        when(contactMapper.toDto(contacts)).thenReturn(List.of(contactDTO));
        when(contactRepository.findAll()).thenReturn(contacts);

        // when
        var result = contactService.getAllContacts();

        // then
        assertEquals(1, result.size());
        assertEquals("+123456789", result.get(0).getPhone());
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void testGetContactById() {
        // given
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));
        when(contactMapper.toDto(contact)).thenReturn(contactDTO);

        // when
        var result = contactService.getContactById(1L);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(contactRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveContact() {
        // given
        doReturn(contact).when(contactMapper).toEntity(contactDTO);
        doReturn(contact).when(contactRepository).save(contact);
        doReturn(contactDTO).when(contactMapper).toDto(contact);

        // when
        var result = contactService.saveContact(contactDTO);

        // then
        assertNotNull(result);
        assertEquals("+123456789", result.getPhone());
        verify(contactRepository).save(contact);
    }

    @Test
    void testDeleteContact() {
        // when
        contactService.deleteContact(1L);

        // then
        verify(contactRepository, times(1)).deleteById(1L);
    }

}