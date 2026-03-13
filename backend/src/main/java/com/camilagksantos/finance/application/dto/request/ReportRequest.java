package com.camilagksantos.finance.application.dto.request;

import com.camilagksantos.finance.domain.model.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReportRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Report title is required")
        String title,

        @NotNull(message = "Report type is required")
        Report.ReportType type,

        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate
) { }
