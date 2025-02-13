package com.example.clients.service;

import com.example.clients.dto.ClientDTO;
import com.example.clients.mapper.ClientMapper;
import com.example.clients.model.Client;
import com.example.clients.repository.ClientRepository;
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
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private static Client client;
    private static ClientDTO clientDTO;

    @BeforeAll
    static void beforeAll() {
        client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        clientDTO = new ClientDTO();
        clientDTO.setClientId(1L);
        clientDTO.setName("John");
        clientDTO.setLastName("Doe");
    }

    @Test
    void testGetAllClients() {
        //given
        var clients = List.of(client);
        when(clientMapper.toDto(clients)).thenReturn(List.of(clientDTO));
        when(clientRepository.findAll()).thenReturn(clients);

        //when
        var result = clientService.getAllClients();

        //then
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        //given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        //when
        var result = clientService.getClientById(1L);

        //then
        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveClient() {
        //given
        doReturn(client).when(clientMapper).toEntity(clientDTO);
        doReturn(client).when(clientRepository).save(client);
        doReturn(clientDTO).when(clientMapper).toDto(client);

        //when
        var result = clientService.saveClient(clientDTO);

        //then
        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(clientRepository).save(client);
    }

    @Test
    void testDeleteClient() {
        //when
        clientService.deleteClient(1L);

        //then
        verify(clientRepository, times(1)).deleteById(1L);
    }

}
