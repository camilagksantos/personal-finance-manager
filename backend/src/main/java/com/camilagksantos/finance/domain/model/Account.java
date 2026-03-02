package com.camilagksantos.finance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Account(UUID id,
                      Long userId,
                      String name,
                      AccountType type,
                      BigDecimal balance,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt
) {
    public enum AccountType {
        CHECKING, SAVINGS, CREDIT_CARD
    }
}
