package com.camilagksantos.finance.infrastructure.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BalanceEvolutionData(
        String accountName,
        BigDecimal balance,
        LocalDate date
) {}