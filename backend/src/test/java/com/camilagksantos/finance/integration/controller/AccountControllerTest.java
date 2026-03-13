package com.camilagksantos.finance.integration.controller;

import com.camilagksantos.finance.application.dto.request.AccountRequest;
import com.camilagksantos.finance.domain.model.Account.AccountType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private AccountRequest validRequest;
    private String createdAccountId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        validRequest = new AccountRequest(
                1L,
                "Conta Corrente",
                AccountType.CHECKING,
                new BigDecimal("1500.00")
        );

        String response = mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdAccountId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    void create_shouldReturn201WithCreatedAccount() throws Exception {
        AccountRequest request = new AccountRequest(
                1L,
                "Poupança",
                AccountType.SAVINGS,
                new BigDecimal("3000.00")
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Poupança"))
                .andExpect(jsonPath("$.type").value("SAVINGS"))
                .andExpect(jsonPath("$.balance").value(3000.00));
    }

    @Test
    void create_shouldReturn400WhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200WithAccount() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{id}", createdAccountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAccountId))
                .andExpect(jsonPath("$.name").value("Conta Corrente"))
                .andExpect(jsonPath("$.type").value("CHECKING"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByUserId_shouldReturn200WithAccountList() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void getAllByUserId_shouldReturn200WithEmptyListWhenNoAccounts() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/user/{userId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void update_shouldReturn200WithUpdatedAccount() throws Exception {
        AccountRequest updateRequest = new AccountRequest(
                1L,
                "Conta Actualizada",
                AccountType.CHECKING,
                new BigDecimal("2000.00")
        );

        mockMvc.perform(put("/api/v1/accounts/{id}", createdAccountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdAccountId))
                .andExpect(jsonPath("$.name").value("Conta Actualizada"))
                .andExpect(jsonPath("$.balance").value(1500.00));
    }

    @Test
    void update_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/accounts/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/{id}", createdAccountId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/accounts/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}