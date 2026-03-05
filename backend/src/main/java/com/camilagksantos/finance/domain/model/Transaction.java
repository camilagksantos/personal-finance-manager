package com.camilagksantos.finance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(
        UUID id,
        UUID accountId,
        UUID categoryId,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDate date,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public enum TransactionType {
        INCOME, EXPENSE
    }
}
