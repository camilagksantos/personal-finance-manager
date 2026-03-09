package com.camilagksantos.finance.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCredentials(
        UUID id,
        String username,
        String password,
        Long userId,
        LocalDateTime createdAt
) {}