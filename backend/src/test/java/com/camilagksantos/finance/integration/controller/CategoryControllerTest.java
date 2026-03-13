package com.camilagksantos.finance.integration.controller;

import com.camilagksantos.finance.application.dto.request.CategoryRequest;
import com.camilagksantos.finance.domain.model.Category.CategoryType;
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
class CategoryControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private MockMvc mockMvc;
    private CategoryRequest validRequest;
    private String createdCategoryId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        validRequest = new CategoryRequest(
                1L,
                "Alimentação",
                CategoryType.EXPENSE
        );

        String response = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        createdCategoryId = objectMapper.readTree(response).get("id").asText();
    }

    @Test
    void create_shouldReturn201WithCreatedCategory() throws Exception {
        CategoryRequest request = new CategoryRequest(
                1L,
                "Salário",
                CategoryType.INCOME
        );

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Salário"))
                .andExpect(jsonPath("$.type").value("INCOME"));
    }

    @Test
    void create_shouldReturn400WhenBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturn200WithCategory() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", createdCategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCategoryId))
                .andExpect(jsonPath("$.name").value("Alimentação"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/categories/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllByUserId_shouldReturn200WithCategoryList() throws Exception {
        mockMvc.perform(get("/api/v1/categories/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1));
    }

    @Test
    void getAllByUserId_shouldReturn200WithEmptyListWhenNoCategories() throws Exception {
        mockMvc.perform(get("/api/v1/categories/user/{userId}", 999L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void update_shouldReturn200WithUpdatedCategory() throws Exception {
        CategoryRequest updateRequest = new CategoryRequest(
                1L,
                "Categoria Actualizada",
                CategoryType.EXPENSE
        );

        mockMvc.perform(put("/api/v1/categories/{id}", createdCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCategoryId))
                .andExpect(jsonPath("$.name").value("Categoria Actualizada"))
                .andExpect(jsonPath("$.type").value("EXPENSE"));
    }

    @Test
    void update_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(put("/api/v1/categories/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", createdCategoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}