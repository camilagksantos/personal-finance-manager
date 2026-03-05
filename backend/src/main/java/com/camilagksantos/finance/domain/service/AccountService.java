package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.AccountNotFoundException;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.ports.input.AccountUseCase;
import com.camilagksantos.finance.domain.ports.output.AccountOutputPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService implements AccountUseCase {

    private final AccountOutputPort accountOutputPort;

    public AccountService(AccountOutputPort accountOutputPort) {
        this.accountOutputPort = accountOutputPort;
    }

    @Override
    public Account create(Account account) {
        Account newAccount = new Account(
            UUID.randomUUID(),
            account.userId(),
            account.name(),
            account.type(),
            account.balance(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return accountOutputPort.save(newAccount);
    }

    @Override
    public Account getById(UUID id) {
        return accountOutputPort.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public List<Account> getAllByUserId(Long userId) {
        return accountOutputPort.findAllByUserId(userId);
    }

    @Override
    public Account update(UUID id, Account account) {
        Account existing = getById(id);
        Account updated = new Account(
                existing.id(),
                existing.userId(),
                account.name(),
                account.type(),
                existing.balance(),
                existing.createdAt(),
                LocalDateTime.now()
        );
        return accountOutputPort.save(updated);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        accountOutputPort.deleteById(id);
    }
}
