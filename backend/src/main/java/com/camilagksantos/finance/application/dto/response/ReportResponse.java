package com.camilagksantos.finance.application.dto.response;

import com.camilagksantos.finance.domain.model.Report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponse(
        UUID id,
        Long userId,
        String title,
        Report.ReportType type,
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime generatedAt
) { }
