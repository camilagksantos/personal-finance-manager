package com.camilagksantos.finance.application.dto.request;

import com.camilagksantos.finance.domain.model.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Report title is required")
        String title,

        @NotNull(message = "Report type is required")
        Report.ReportType type
) { }
