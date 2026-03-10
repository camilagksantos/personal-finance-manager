package com.camilagksantos.finance.e2e.controller;

import com.camilagksantos.finance.application.dto.request.AccountRequest;
import com.camilagksantos.finance.application.dto.request.CategoryRequest;
import com.camilagksantos.finance.application.dto.request.TransactionRequest;
import com.camilagksantos.finance.domain.model.Account.AccountType;
import com.camilagksantos.finance.domain.model.Category.CategoryType;
import com.camilagksantos.finance.domain.model.Transaction.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private MockMvc mockMvc;
    private TransactionRequest validRequest;
    private String createdTransactionId;
    private String createdAccountId;
    private String createdCategoryId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        String accountResponse = mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AccountRequest(
                                1L,
                                "Conta Corrente",
                                AccountType.CHECKING,
                                new BigDecimal("1500.00")
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        createdAccountId = objectMapper.readTree(accountResponse).get("id").asText();

        String categoryResponse = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest(
                                1L,
                                "Alimentação",
                                CategoryType.EXPENSE
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        createdCategoryId = objectMapper.readTree(categoryResponse).get("id").asText();

        validRequest = new TransactionRequest(
                UUID.fromString(createdAccountId),
                UUID.fromString(createdCategoryId),
                new BigDecimal("100.00"),
                TransactionType.EXPENSE,
                "Supermercado",
                LocalDate.now()
        );

        String transactionResponse = mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        createdTransactionId = objectMapper.readTree(transactionResponse).get("id").asText();
    }

    @Test
    void create_shouldReturn201WithCreatedTransaction() throws Exception {
        TransactionRequest request = new TransactionRequest(
                UUID.fromString(createdAccountId),
                UUID.fromString(createdCategoryId),
                new BigDecimal("200.00"),
                TransactionType.EXPENSE,
                "Restaurante",
                LocalDate.now()
        );

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.description").value("Restaurante"));
    }

    @Test
    void create_shouldReturn400WhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200WithTransaction() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{id}", createdTransactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTransactionId))
                .andExpect(jsonPath("$.description").value("Supermercado"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByAccountId_shouldReturn200WithTransactionList() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/account/{accountId}", createdAccountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].accountId").value(createdAccountId));
    }

    @Test
    void getAllByAccountId_shouldReturn200WithEmptyListWhenNoTransactions() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/account/{accountId}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByPeriod_shouldReturn200WithTransactionList() throws Exception {
        String startDate = LocalDate.now().minusDays(1).toString();
        String endDate = LocalDate.now().plusDays(1).toString();

        mockMvc.perform(get("/api/v1/transactions/account/{accountId}/period", createdAccountId)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(createdTransactionId));
    }

    @Test
    void getByPeriod_shouldReturn200WithEmptyListWhenOutOfRange() throws Exception {
        String startDate = LocalDate.now().minusDays(30).toString();
        String endDate = LocalDate.now().minusDays(10).toString();

        mockMvc.perform(get("/api/v1/transactions/account/{accountId}/period", createdAccountId)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void update_shouldReturn200WithUpdatedTransaction() throws Exception {
        TransactionRequest updateRequest = new TransactionRequest(
                UUID.fromString(createdAccountId),
                UUID.fromString(createdCategoryId),
                new BigDecimal("150.00"),
                TransactionType.EXPENSE,
                "Supermercado Actualizado",
                LocalDate.now()
        );

        mockMvc.perform(put("/api/v1/transactions/{id}", createdTransactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTransactionId))
                .andExpect(jsonPath("$.description").value("Supermercado Actualizado"))
                .andExpect(jsonPath("$.amount").value(150.00));
    }

    @Test
    void update_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/transactions/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/transactions/{id}", createdTransactionId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}