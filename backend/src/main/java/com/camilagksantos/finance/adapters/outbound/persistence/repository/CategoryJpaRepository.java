package com.camilagksantos.finance.adapters.outbound.persistence.repository;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
    List<CategoryEntity> findAllByUserId(Long userId);
}
