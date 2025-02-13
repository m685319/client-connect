package com.example.clients.service;

import com.example.clients.dto.ClientDTO;
import com.example.clients.mapper.ClientMapper;
import com.example.clients.model.Client;
import com.example.clients.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public List<ClientDTO> getAllClients() {
        return clientMapper.toDto(clientRepository.findAll());
    }

    public ClientDTO getClientById(Long clientId) {
        return clientMapper.toDto(clientRepository.findById(clientId).orElseThrow(EntityNotFoundException::new));
    }

    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = clientMapper.toEntity(clientDTO);
        return clientMapper.toDto(clientRepository.save(client));
    }

    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();
            client.setName(clientDTO.getName());
            client.setLastName(clientDTO.getLastName());
            client = clientRepository.save(client);
            return clientMapper.toDto(client);
        } else {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }
    }
}
