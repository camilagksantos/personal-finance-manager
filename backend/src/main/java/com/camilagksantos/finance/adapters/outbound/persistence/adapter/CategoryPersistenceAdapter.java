package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.mapper.CategoryEntityMapper;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.CategoryJpaRepository;
import com.camilagksantos.finance.domain.model.Category;
import com.camilagksantos.finance.domain.ports.output.CategoryOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CategoryPersistenceAdapter implements CategoryOutputPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    public CategoryPersistenceAdapter(CategoryJpaRepository categoryJpaRepository, CategoryEntityMapper categoryEntityMapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.categoryEntityMapper = categoryEntityMapper;
    }

    @Override
    public Category save(Category category) {
        return categoryEntityMapper.toDomain(
                categoryJpaRepository.save(categoryEntityMapper.toEntity(category))
        );
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        return categoryJpaRepository.findAllByUserId(userId)
                .stream()
                .map(categoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        categoryJpaRepository.deleteById(id);
    }
}