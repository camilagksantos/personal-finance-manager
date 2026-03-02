package com.camilagksantos.finance.domain.model;

public record User(
        Long id,
        String name,
        String username,
        String email,
        String phone
) {}
