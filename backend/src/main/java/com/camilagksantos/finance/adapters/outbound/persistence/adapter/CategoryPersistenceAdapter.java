package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.CategoryEntity;
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

    public CategoryPersistenceAdapter(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        return categoryJpaRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        categoryJpaRepository.deleteById(id);
    }

    private CategoryEntity toEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.id());
        entity.setUserId(category.userId());
        entity.setName(category.name());
        entity.setType(category.type());
        entity.setCreatedAt(category.createdAt());
        entity.setUpdatedAt(category.updatedAt());
        return entity;
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getType(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}