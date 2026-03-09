package com.camilagksantos.finance.unit.domain.service;

import com.camilagksantos.finance.domain.exception.CategoryNotFoundException;
import com.camilagksantos.finance.domain.model.Category;
import com.camilagksantos.finance.domain.ports.output.CategoryOutputPort;
import com.camilagksantos.finance.domain.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryOutputPort categoryOutputPort;

    @InjectMocks
    private CategoryService categoryService;

    private UUID categoryId;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        category = new Category(
                categoryId,
                1L,
                "Alimentação",
                Category.CategoryType.EXPENSE,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateCategory() {
        when(categoryOutputPort.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.create(category);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Alimentação");
        verify(categoryOutputPort, times(1)).save(any(Category.class));
    }

    @Test
    void shouldGetCategoryById() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(categoryId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(categoryId);
        verify(categoryOutputPort, times(1)).findById(categoryId);
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(categoryId))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryOutputPort, times(1)).findById(categoryId);
    }

    @Test
    void shouldGetAllCategoriesByUserId() {
        when(categoryOutputPort.findAllByUserId(1L)).thenReturn(List.of(category));

        List<Category> result = categoryService.getAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
        verify(categoryOutputPort, times(1)).findAllByUserId(1L);
    }

    @Test
    void shouldUpdateCategory() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryOutputPort.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.update(categoryId, category);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(categoryId);
        verify(categoryOutputPort, times(1)).findById(categoryId);
        verify(categoryOutputPort, times(1)).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCategory() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.update(categoryId, category))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryOutputPort, never()).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategory() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.delete(categoryId);

        verify(categoryOutputPort, times(1)).deleteById(categoryId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCategory() {
        when(categoryOutputPort.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(categoryId))
                .isInstanceOf(CategoryNotFoundException.class);
        verify(categoryOutputPort, never()).deleteById(any());
    }
}