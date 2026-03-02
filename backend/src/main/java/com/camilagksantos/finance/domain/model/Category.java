package com.camilagksantos.finance.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Category(
        UUID id,
        Long userId,
        String name,
        CategoryType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public enum CategoryType {
        INCOME, EXPENSE
    }
}
