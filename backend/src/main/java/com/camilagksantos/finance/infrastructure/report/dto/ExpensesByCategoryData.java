package com.camilagksantos.finance.infrastructure.report.dto;

import java.math.BigDecimal;

public record ExpensesByCategoryData(
        String categoryId,
        BigDecimal totalAmount
) {}