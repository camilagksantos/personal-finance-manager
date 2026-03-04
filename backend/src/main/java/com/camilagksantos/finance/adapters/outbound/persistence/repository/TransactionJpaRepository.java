package com.camilagksantos.finance.adapters.outbound.persistence.repository;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findAllByAccount_Id(UUID accountId);

    List<TransactionEntity> findAllByAccount_IdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate);
}
