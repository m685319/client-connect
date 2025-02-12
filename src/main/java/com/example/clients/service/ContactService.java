package com.example.clients.service;

import com.example.clients.dto.ContactDTO;
import com.example.clients.exception.EntityNotFoundException;
import com.example.clients.mapper.ContactMapper;
import com.example.clients.model.Contact;
import com.example.clients.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    public List<ContactDTO> getAllContacts() {
        return contactMapper.toDto(contactRepository.findAll());
    }

    public ContactDTO getContactById(Long id) {
        return contactMapper.toDto(contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact with id " + id + " not found")));
    }

    public ContactDTO saveContact(ContactDTO contactDTO) {
        Contact contact = contactMapper.toEntity(contactDTO);
        return contactMapper.toDto(contactRepository.save(contact));
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact with id " + id + " not found"));

        contact.setPhone(contactDTO.getPhone());
        contact.setEmail(contactDTO.getEmail());

        return contactMapper.toDto(contactRepository.save(contact));
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
