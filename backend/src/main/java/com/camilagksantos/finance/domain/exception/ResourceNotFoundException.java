package com.camilagksantos.finance.domain.exception;

public class ResourceNotFoundException extends DomainException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
