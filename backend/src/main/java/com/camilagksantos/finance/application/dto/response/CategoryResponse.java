package com.camilagksantos.finance.application.dto.response;

import com.camilagksantos.finance.domain.model.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        Long userId,
        String name,
        Category.CategoryType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
