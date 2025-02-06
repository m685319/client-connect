package com.example.clients.service;

import com.example.clients.model.Contact;
import com.example.clients.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long contactId) {
        return contactRepository.findById(contactId);
    }

    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Optional<Contact> updateContact(Long id, Contact updatedContact) {
        return contactRepository.findById(id).map(existingContact -> {
            existingContact.setPhone(updatedContact.getPhone());
            existingContact.setEmail(updatedContact.getEmail());
            return contactRepository.save(existingContact);
        });
    }

    public void deleteContact(Long contactId) {
        contactRepository.deleteById(contactId);
    }
}
