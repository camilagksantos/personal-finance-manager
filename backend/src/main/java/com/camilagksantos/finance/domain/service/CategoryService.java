package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.CategoryNotFoundException;
import com.camilagksantos.finance.domain.model.Category;
import com.camilagksantos.finance.domain.ports.input.CategoryUseCase;
import com.camilagksantos.finance.domain.ports.output.CategoryOutputPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService implements CategoryUseCase {

    private final CategoryOutputPort categoryOutputPort;

    public CategoryService(CategoryOutputPort categoryOutputPort) {
        this.categoryOutputPort = categoryOutputPort;
    }

    @Override
    public Category create(Category category) {
        Category newCategory = new Category(
                UUID.randomUUID(),
                category.userId(),
                category.name(),
                category.type(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return categoryOutputPort.save(newCategory);
    }

    @Override
    public Category getById(UUID id) {
        return categoryOutputPort.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public List<Category> getAllByUserId(Long userId) {
        return categoryOutputPort.findAllByUserId(userId);
    }

    @Override
    public Category update(UUID id, Category category) {
        Category existing = getById(id);
        Category updated = new Category(
                existing.id(),
                existing.userId(),
                category.name(),
                category.type(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return categoryOutputPort.save(updated);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        categoryOutputPort.deleteById(id);
    }
}
