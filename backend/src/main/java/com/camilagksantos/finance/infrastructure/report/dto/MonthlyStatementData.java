package com.camilagksantos.finance.infrastructure.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MonthlyStatementData(
        String description,
        String type,
        BigDecimal amount,
        LocalDate date
) {}