package com.camilagksantos.finance.unit.application.mapper;

import com.camilagksantos.finance.application.dto.request.CategoryRequest;
import com.camilagksantos.finance.application.dto.response.CategoryResponse;
import com.camilagksantos.finance.application.mapper.CategoryMapperImpl;
import com.camilagksantos.finance.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryMapperTest {

    @InjectMocks
    private CategoryMapperImpl mapper;

    @Test
    void shouldMapRequestToDomain() {
        CategoryRequest request = new CategoryRequest(
                1L,
                "Alimentação",
                Category.CategoryType.EXPENSE
        );

        Category result = mapper.toDomain(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Alimentação");
        assertThat(result.type()).isEqualTo(Category.CategoryType.EXPENSE);
        assertThat(result.id()).isNull();
        assertThat(result.createdAt()).isNull();
        assertThat(result.updatedAt()).isNull();
    }

    @Test
    void shouldMapDomainToResponse() {
        Category category = new Category(
                UUID.randomUUID(),
                1L,
                "Alimentação",
                Category.CategoryType.EXPENSE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CategoryResponse result = mapper.toResponse(category);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(category.id());
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Alimentação");
        assertThat(result.type()).isEqualTo(Category.CategoryType.EXPENSE);
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();
    }
}