package com.camilagksantos.finance.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Report(
        UUID id,
        Long userId,
        String title,
        ReportType type,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime generatedAt
) {
    public enum ReportType {
        MONTHLY_STATEMENT, EXPENSES_BY_CATEGORY, BALANCE_EVOLUTION
    }
}