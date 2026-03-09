package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.UserCredentials;

public interface UserCredentialsUseCase {

    UserCredentials register(UserCredentials userCredentials);

    UserCredentials findByUsername(String username);
}