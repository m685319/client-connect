package com.example.clients.service;

import com.example.clients.model.Client;
import com.example.clients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = Arrays.asList(client);
        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> result = clientService.getAllClients();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClientById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveClient() {
        when(clientRepository.save(client)).thenReturn(client);

        Client result = clientService.saveClient(client);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }

}