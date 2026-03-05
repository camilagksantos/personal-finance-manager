package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryUseCase {

    Category create(Category category);

    Category getById(UUID id);

    List<Category> getAllByUserId(Long userId);

    Category update(UUID id, Category category);

    void delete(UUID id);
}
