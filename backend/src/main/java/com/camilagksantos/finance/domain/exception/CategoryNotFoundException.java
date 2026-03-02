package com.camilagksantos.finance.domain.exception;

import java.util.UUID;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(UUID id) {
        super("Category not found with id: " + id);
    }
}