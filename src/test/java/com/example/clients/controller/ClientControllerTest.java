package com.example.clients.controller;

import com.example.clients.model.Client;
import com.example.clients.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");
    }

    @Test
    void testGetAllClients() throws Exception {
        List<Client> clients = Arrays.asList(client);
        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testGetClientById_NotFound() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void testCreateClient() throws Exception {
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));

        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));
        when(clientService.saveClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));

        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(1)).saveClient(any(Client.class));
    }

    @Test
    void testUpdateClient_NotFound() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientById(1L);
        verify(clientService, times(0)).saveClient(any(Client.class));
    }

    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(1L);
    }

}