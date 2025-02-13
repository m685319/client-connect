package com.example.clients.mapper;

import com.example.clients.dto.ClientDTO;
import com.example.clients.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClientMapper {

    @Mapping(source = "contact.id", target = "contactId")
    ClientDTO toDto(Client client);

    Client toEntity(ClientDTO clientDTO);

    List<ClientDTO> toDto(List<Client> clients);
}
