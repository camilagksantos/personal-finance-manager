package com.camilagksantos.finance.infrastructure.config;

import com.camilagksantos.finance.domain.model.UserCredentials;
import com.camilagksantos.finance.domain.ports.input.UserCredentialsUseCase;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialsUseCase userCredentialsUseCase;

    public UserDetailsServiceImpl(UserCredentialsUseCase userCredentialsUseCase) {
        this.userCredentialsUseCase = userCredentialsUseCase;
    }

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        UserCredentials credentials = userCredentialsUseCase.findByUsername(username);

        return User.builder()
                .username(credentials.username())
                .password(credentials.password())
                .roles("USER")
                .build();
    }
}