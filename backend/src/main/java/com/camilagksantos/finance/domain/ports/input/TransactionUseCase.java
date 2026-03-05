package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionUseCase {

    Transaction create(Transaction transaction);

    Transaction getById(UUID id);

    List<Transaction> getAllByAccountId(UUID accountId);

    List<Transaction> getAllByAccountIdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate);

    Transaction update(UUID id, Transaction transaction);

    void delete(UUID id);
}
