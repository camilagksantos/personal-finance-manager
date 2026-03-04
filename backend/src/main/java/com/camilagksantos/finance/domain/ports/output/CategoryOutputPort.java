package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryOutputPort {

    Category save(Category category);

    Optional<Category> findById(UUID id);

    List<Category> findAllByUserId(Long userId);

    void deleteById(UUID id);
}
