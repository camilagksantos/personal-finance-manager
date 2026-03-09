package com.camilagksantos.finance.adapters.outbound.persistence.repository;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.UserCredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsJpaRepository extends JpaRepository<UserCredentialsEntity, UUID> {

    Optional<UserCredentialsEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);
}