package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.TransactionNotFoundException;
import com.camilagksantos.finance.domain.model.Transaction;
import com.camilagksantos.finance.domain.ports.input.TransactionUseCase;
import com.camilagksantos.finance.domain.ports.output.TransactionOutputPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService implements TransactionUseCase {
    private final TransactionOutputPort transactionOutputPort;

    public TransactionService(TransactionOutputPort transactionOutputPort) {
        this.transactionOutputPort = transactionOutputPort;
    }

    @Override
    public Transaction create(Transaction transaction) {
        Transaction newTransaction = new Transaction(
                UUID.randomUUID(),
                transaction.accountId(),
                transaction.categoryId(),
                transaction.amount(),
                transaction.type(),
                transaction.description(),
                transaction.date(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return transactionOutputPort.save(newTransaction);
    }

    @Override
    public Transaction getById(UUID id) {
        return transactionOutputPort.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public List<Transaction> getAllByAccountId(UUID accountId) {
        return transactionOutputPort.findAllByAccountId(accountId);
    }

    @Override
    public List<Transaction> getAllByAccountIdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate) {
        return transactionOutputPort.findAllByAccountIdAndDateBetween(accountId, startDate, endDate);
    }

    @Override
    public Transaction update(UUID id, Transaction transaction) {
        Transaction existing = getById(id);
        Transaction updated = new Transaction(
                existing.id(),
                existing.accountId(),
                existing.categoryId(),
                transaction.amount(),
                transaction.type(),
                transaction.description(),
                transaction.date(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return transactionOutputPort.save(updated);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        transactionOutputPort.deleteById(id);
    }
}
