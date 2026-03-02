package com.camilagksantos.finance.domain.exception;

import java.util.UUID;

public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException(UUID id) {
        super("Account not found with id: " + id);
    }
}