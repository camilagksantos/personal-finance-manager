package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountOutputPort {

    Account save(Account account);

    Optional<Account> findById(UUID id);

    List<Account> findAllByUserId(Long userId);

    void deleteById(UUID id);
}
