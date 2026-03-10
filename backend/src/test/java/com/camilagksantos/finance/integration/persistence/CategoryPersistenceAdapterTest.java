package com.camilagksantos.finance.integration.persistence;

import com.camilagksantos.finance.adapters.outbound.persistence.adapter.CategoryPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.CategoryJpaRepository;
import com.camilagksantos.finance.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryPersistenceAdapterTest {

    @Autowired
    private CategoryPersistenceAdapter categoryPersistenceAdapter;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        categoryJpaRepository.deleteAll();
        category = new Category(
                UUID.randomUUID(),
                1L,
                "Alimentacao",
                Category.CategoryType.EXPENSE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldSaveCategory() {
        Category result = categoryPersistenceAdapter.save(category);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(category.id());
        assertThat(result.name()).isEqualTo("Alimentacao");
    }

    @Test
    void shouldFindCategoryById() {
        categoryPersistenceAdapter.save(category);

        Optional<Category> result = categoryPersistenceAdapter.findById(category.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(category.id());
    }

    @Test
    void shouldReturnEmptyWhenCategoryNotFound() {
        Optional<Category> result = categoryPersistenceAdapter.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAllCategoriesByUserId() {
        categoryPersistenceAdapter.save(category);

        List<Category> result = categoryPersistenceAdapter.findAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteCategoryById() {
        categoryPersistenceAdapter.save(category);

        categoryPersistenceAdapter.deleteById(category.id());

        Optional<Category> result = categoryPersistenceAdapter.findById(category.id());
        assertThat(result).isEmpty();
    }
}