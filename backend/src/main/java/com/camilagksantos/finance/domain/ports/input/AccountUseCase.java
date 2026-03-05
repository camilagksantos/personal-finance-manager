package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountUseCase {

    Account create(Account account);

    Account getById(UUID id);

    List<Account> getAllByUserId(Long userId);

    Account update(UUID id, Account account);

    void delete(UUID id);
}
