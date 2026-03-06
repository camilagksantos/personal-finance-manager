package com.camilagksantos.finance.application.dto.request;

import com.camilagksantos.finance.domain.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AccountRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Account name is required")
        String name,

        @NotNull(message = "Account type is required")
        Account.AccountType type,

        @NotNull(message = "Balance is required")
        @PositiveOrZero(message = "Balance must be zero or positive")
        BigDecimal balance
) { }
