package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.mapper.TransactionEntityMapper;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.TransactionJpaRepository;
import com.camilagksantos.finance.domain.model.Transaction;
import com.camilagksantos.finance.domain.ports.output.TransactionOutputPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionPersistenceAdapter implements TransactionOutputPort {

    private final TransactionJpaRepository transactionJpaRepository;
    private final TransactionEntityMapper transactionEntityMapper;

    public TransactionPersistenceAdapter(TransactionJpaRepository transactionJpaRepository,
                                         TransactionEntityMapper transactionEntityMapper) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.transactionEntityMapper = transactionEntityMapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionEntityMapper.toDomain(
                transactionJpaRepository.save(transactionEntityMapper.toEntity(transaction))
        );
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionJpaRepository.findById(id)
                .map(transactionEntityMapper::toDomain);
    }

    @Override
    public List<Transaction> findAllByAccountId(UUID accountId) {
        return transactionJpaRepository.findAllByAccount_Id(accountId)
                .stream()
                .map(transactionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findAllByAccountIdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate) {
        return transactionJpaRepository.findAllByAccount_IdAndDateBetween(accountId, startDate, endDate)
                .stream()
                .map(transactionEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        transactionJpaRepository.deleteById(id);
    }
}