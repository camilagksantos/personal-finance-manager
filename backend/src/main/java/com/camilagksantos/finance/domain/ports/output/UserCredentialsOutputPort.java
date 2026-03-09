package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.UserCredentials;

import java.util.Optional;

public interface UserCredentialsOutputPort {

    UserCredentials save(UserCredentials userCredentials);

    Optional<UserCredentials> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);
}