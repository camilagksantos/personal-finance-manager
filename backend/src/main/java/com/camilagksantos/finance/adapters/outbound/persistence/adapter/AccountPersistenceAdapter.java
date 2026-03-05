package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.AccountEntity;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.AccountJpaRepository;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.ports.output.AccountOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccountPersistenceAdapter implements AccountOutputPort {

    private final AccountJpaRepository accountJpaRepository;

    public AccountPersistenceAdapter(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = toEntity(account);
        AccountEntity saved = accountJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Account> findAllByUserId(Long userId) {
        return accountJpaRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        accountJpaRepository.deleteById(id);
    }

    private AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.id());
        entity.setUserId(account.userId());
        entity.setName(account.name());
        entity.setType(account.type());
        entity.setBalance(account.balance());
        entity.setCreatedAt(account.createdAt());
        entity.setUpdatedAt(account.updatedAt());
        return entity;
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getUserId(),
                entity.getName(),
                entity.getType(),
                entity.getBalance(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}