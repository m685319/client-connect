package com.example.clients.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class ClientDTO {
    private Long clientId;
    private String name;
    private String lastName;
    private Long contactId;
}
