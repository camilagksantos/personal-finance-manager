package com.camilagksantos.finance.application.dto.response;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {}