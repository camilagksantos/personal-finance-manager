package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionOutputPort {

    Transaction save(Transaction transaction);

    Optional<Transaction> findById(UUID id);

    List<Transaction> findAllByAccountId(UUID accountId);

    List<Transaction> findAllByAccountIdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate);

    void deleteById(UUID id);
}
