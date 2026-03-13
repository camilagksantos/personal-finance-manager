package com.camilagksantos.finance.integration.controller;

import com.camilagksantos.finance.application.dto.request.ReportRequest;
import com.camilagksantos.finance.domain.model.Report.ReportType;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
@Transactional
class ReportControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private MockMvc mockMvc;
    private ReportRequest validRequest;
    private String createdReportId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        validRequest = new ReportRequest(
                1L,
                "Relatório Mensal",
                ReportType.MONTHLY_STATEMENT
        );

        String response = mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        createdReportId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    void generate_shouldReturn201WithCreatedReport() throws Exception {
        ReportRequest request = new ReportRequest(
                1L,
                "Despesas por Categoria",
                ReportType.EXPENSES_BY_CATEGORY
        );

        mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Despesas por Categoria"))
                .andExpect(jsonPath("$.type").value("EXPENSES_BY_CATEGORY"));
    }

    @Test
    void generate_shouldReturn400WhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200WithReport() throws Exception {
        mockMvc.perform(get("/api/v1/reports/{id}", createdReportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdReportId))
                .andExpect(jsonPath("$.title").value("Relatório Mensal"))
                .andExpect(jsonPath("$.type").value("MONTHLY_STATEMENT"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/reports/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByUserId_shouldReturn200WithReportList() throws Exception {
        mockMvc.perform(get("/api/v1/reports/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void getAllByUserId_shouldReturn200WithEmptyListWhenNoReports() throws Exception {
        mockMvc.perform(get("/api/v1/reports/user/{userId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/reports/{id}", createdReportId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/reports/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}