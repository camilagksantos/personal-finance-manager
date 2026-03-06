package com.camilagksantos.finance.application.dto.response;

import com.camilagksantos.finance.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID categoryId,
        BigDecimal amount,
        Transaction.TransactionType type,
        String description,
        LocalDate date,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
