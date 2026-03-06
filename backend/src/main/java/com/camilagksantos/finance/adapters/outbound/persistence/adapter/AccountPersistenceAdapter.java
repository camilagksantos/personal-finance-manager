package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.mapper.AccountEntityMapper;
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
    private final AccountEntityMapper accountEntityMapper;

    public AccountPersistenceAdapter(AccountJpaRepository accountJpaRepository,
                                     AccountEntityMapper accountEntityMapper) {
        this.accountJpaRepository = accountJpaRepository;
        this.accountEntityMapper = accountEntityMapper;
    }

    @Override
    public Account save(Account account) {
        return accountEntityMapper.toDomain(
                accountJpaRepository.save(accountEntityMapper.toEntity(account))
        );
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return accountJpaRepository.findById(id)
                .map(accountEntityMapper::toDomain);
    }

    @Override
    public List<Account> findAllByUserId(Long userId) {
        return accountJpaRepository.findAllByUserId(userId)
                .stream()
                .map(accountEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        accountJpaRepository.deleteById(id);
    }
}