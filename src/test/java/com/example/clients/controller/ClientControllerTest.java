package com.example.clients.controller;

import com.example.clients.dto.ClientDTO;
import com.example.clients.exception.EntityNotFoundException;
import com.example.clients.exception.GlobalExceptionHandler;
import com.example.clients.service.ClientService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    private static final Long CLIENT_ID = 1L;

    private static MockMvc mockMvc;

    private static ClientService clientService;

    private static ClientDTO clientDTO;

    @BeforeAll
    static void beforeAll() {
        clientService = mock(ClientService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ClientController(clientService))
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();

        clientDTO = new ClientDTO();
        clientDTO.setClientId(CLIENT_ID);
        clientDTO.setName("John");
        clientDTO.setLastName("Doe");
    }

    @AfterEach
    void tearDown() {
        reset(clientService);
    }

    @Test
    void testGetAllClients() throws Exception {
        var clients = Arrays.asList(clientDTO);
        when(clientService.getAllClients()).thenReturn(clients);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    void testGetClientById() throws Exception {
        when(clientService.getClientById(CLIENT_ID)).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(clientService, times(1)).getClientById(CLIENT_ID);
    }

    @Test
    void testGetClientById_NotFound() throws Exception {
        when(clientService.getClientById(CLIENT_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientById(CLIENT_ID);
    }

    @Test
    void testCreateClient() throws Exception {
        when(clientService.saveClient(any(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));

        verify(clientService, times(1)).saveClient(any(ClientDTO.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        when(clientService.getClientById(CLIENT_ID)).thenReturn(clientDTO);
        when(clientService.updateClient(eq(CLIENT_ID), any(ClientDTO.class))).thenReturn(clientDTO);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));

        verify(clientService, times(1)).updateClient(eq(CLIENT_ID), any(ClientDTO.class));
    }

    @Test
    void testUpdateClient_NotFound() throws Exception {
        doThrow(new EntityNotFoundException("Client with id " + CLIENT_ID + " not found")).when(clientService)
                .updateClient(eq(CLIENT_ID), any(ClientDTO.class));

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).updateClient(anyLong(), any(ClientDTO.class));
    }

    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(CLIENT_ID);
    }
}