package com.example.clients.mapper;

import com.example.clients.dto.ContactDTO;
import com.example.clients.model.Contact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDTO toDto(Contact contact);

    Contact toEntity(ContactDTO contactDTO);

    List<ContactDTO> toDto(List<Contact> contacts);
}
