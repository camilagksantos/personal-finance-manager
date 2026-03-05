package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.AccountEntity;
import com.camilagksantos.finance.adapters.outbound.persistence.entity.CategoryEntity;
import com.camilagksantos.finance.adapters.outbound.persistence.entity.TransactionEntity;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.AccountJpaRepository;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.CategoryJpaRepository;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.TransactionJpaRepository;
import com.camilagksantos.finance.domain.exception.AccountNotFoundException;
import com.camilagksantos.finance.domain.exception.CategoryNotFoundException;
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
    private final AccountJpaRepository accountJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public TransactionPersistenceAdapter(
            TransactionJpaRepository transactionJpaRepository,
            AccountJpaRepository accountJpaRepository,
            CategoryJpaRepository categoryJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = toEntity(transaction);
        TransactionEntity saved = transactionJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Transaction> findAllByAccountId(UUID accountId) {
        return transactionJpaRepository.findAllByAccount_Id(accountId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findAllByAccountIdAndDateBetween(UUID accountId, LocalDate startDate, LocalDate endDate) {
        return transactionJpaRepository.findAllByAccount_IdAndDateBetween(accountId, startDate, endDate)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        transactionJpaRepository.deleteById(id);
    }

    private TransactionEntity toEntity(Transaction transaction) {
        AccountEntity account = accountJpaRepository.findById(transaction.accountId())
                .orElseThrow(() -> new AccountNotFoundException(transaction.accountId()));

        CategoryEntity category = categoryJpaRepository.findById(transaction.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(transaction.categoryId()));

        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.id());
        entity.setAccount(account);
        entity.setCategory(category);
        entity.setAmount(transaction.amount());
        entity.setType(transaction.type());
        entity.setDescription(transaction.description());
        entity.setDate(transaction.date());
        entity.setCreatedAt(transaction.createdAt());
        entity.setUpdatedAt(transaction.updatedAt());
        return entity;
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccount().getId(),
                entity.getCategory().getId(),
                entity.getAmount(),
                entity.getType(),
                entity.getDescription(),
                entity.getDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}