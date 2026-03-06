package com.camilagksantos.finance.application.dto.request;

import com.camilagksantos.finance.domain.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Category name is required")
        String name,

        @NotNull(message = "Category type is required")
        Category.CategoryType type
) { }
