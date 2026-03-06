package com.camilagksantos.finance.application.dto.response;

import com.camilagksantos.finance.domain.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        Long userId,
        String name,
        Account.AccountType type,
        BigDecimal balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
