package com.example.clients.service;

import com.example.clients.ClientDTO;
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

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long clientId) {
        return clientRepository.findById(clientId);
    }

    public ClientDTO saveClient(ClientDTO clientDTO) {
        Client client = toEntity(clientDTO);
        return toDTO(clientRepository.save(client));
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
            return toDTO(client);
        } else {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }
    }

    private ClientDTO toDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        if (client.getClientId() != null) {
            clientDTO.setClientId(client.getClientId());
        }
        if (client.getName() != null) {
            clientDTO.setName(client.getName());
        }
        if (client.getLastName() != null) {
            clientDTO.setLastName(client.getLastName());
        }
        return clientDTO;
    }

    private Client toEntity(ClientDTO clientDTO) {
        Client client = new Client();
        if (clientDTO.getClientId() != null) {
            client.setClientId(clientDTO.getClientId());
        }
        if (clientDTO.getName() != null) {
            client.setName(clientDTO.getName());
        }
        if (clientDTO.getLastName() != null) {
            client.setLastName(clientDTO.getLastName());
        }
        return client;
    }
}
