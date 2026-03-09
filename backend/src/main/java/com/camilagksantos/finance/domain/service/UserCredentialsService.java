package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.BusinessRuleException;
import com.camilagksantos.finance.domain.exception.ResourceNotFoundException;
import com.camilagksantos.finance.domain.model.UserCredentials;
import com.camilagksantos.finance.domain.ports.input.UserCredentialsUseCase;
import com.camilagksantos.finance.domain.ports.output.UserCredentialsOutputPort;
import com.camilagksantos.finance.domain.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService implements UserCredentialsUseCase {

    private final UserCredentialsOutputPort userCredentialsOutputPort;
    private final UserOutputPort userOutputPort;

    public UserCredentialsService(UserCredentialsOutputPort userCredentialsOutputPort,
                                  UserOutputPort userOutputPort) {
        this.userCredentialsOutputPort = userCredentialsOutputPort;
        this.userOutputPort = userOutputPort;
    }

    @Override
    public UserCredentials register(UserCredentials userCredentials) {
        if (userCredentialsOutputPort.existsByUsername(userCredentials.username())) {
            throw new BusinessRuleException("Username already in use: " + userCredentials.username());
        }

        if (userCredentialsOutputPort.existsByUserId(userCredentials.userId())) {
            throw new BusinessRuleException("User already has credentials: " + userCredentials.userId());
        }

        userOutputPort.findById(userCredentials.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userCredentials.userId()));

        return userCredentialsOutputPort.save(userCredentials);
    }

    @Override
    public UserCredentials findByUsername(String username) {
        return userCredentialsOutputPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}