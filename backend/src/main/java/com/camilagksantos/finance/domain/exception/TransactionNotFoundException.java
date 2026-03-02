package com.camilagksantos.finance.domain.exception;

import java.util.UUID;

public class TransactionNotFoundException extends ResourceNotFoundException {
    public TransactionNotFoundException(UUID id) {
        super("Transaction not found with id: " + id);
    }
}
