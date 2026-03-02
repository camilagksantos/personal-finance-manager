package com.camilagksantos.finance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Transaction(
        UUID id,
        UUID accountId,
        UUID categoryId,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDate date,
        LocalDate createdAt,
        LocalDate updatedAt
) {
    public enum TransactionType {
        INCOME, EXPENSE
    }
}
