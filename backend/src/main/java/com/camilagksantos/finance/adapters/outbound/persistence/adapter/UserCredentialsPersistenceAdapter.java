package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.mapper.UserCredentialsEntityMapper;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.UserCredentialsJpaRepository;
import com.camilagksantos.finance.domain.model.UserCredentials;
import com.camilagksantos.finance.domain.ports.output.UserCredentialsOutputPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCredentialsPersistenceAdapter implements UserCredentialsOutputPort {

    private final UserCredentialsJpaRepository userCredentialsJpaRepository;
    private final UserCredentialsEntityMapper userCredentialsEntityMapper;

    public UserCredentialsPersistenceAdapter(UserCredentialsJpaRepository userCredentialsJpaRepository,
                                             UserCredentialsEntityMapper userCredentialsEntityMapper) {
        this.userCredentialsJpaRepository = userCredentialsJpaRepository;
        this.userCredentialsEntityMapper = userCredentialsEntityMapper;
    }

    @Override
    public UserCredentials save(UserCredentials userCredentials) {
        return userCredentialsEntityMapper.toDomain(
                userCredentialsJpaRepository.save(userCredentialsEntityMapper.toEntity(userCredentials))
        );
    }

    @Override
    public Optional<UserCredentials> findByUsername(String username) {
        return userCredentialsJpaRepository.findByUsername(username)
                .map(userCredentialsEntityMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userCredentialsJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return userCredentialsJpaRepository.existsByUserId(userId);
    }
}