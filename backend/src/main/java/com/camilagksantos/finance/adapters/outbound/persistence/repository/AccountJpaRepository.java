package com.camilagksantos.finance.adapters.outbound.persistence.repository;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

    List<AccountEntity> findAllByUserId(Long userId);
}
