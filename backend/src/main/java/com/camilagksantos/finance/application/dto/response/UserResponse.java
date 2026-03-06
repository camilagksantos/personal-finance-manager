package com.camilagksantos.finance.application.dto.response;

public record UserResponse(
        Long id,
        String name,
        String username,
        String email,
        String phone
) { }
