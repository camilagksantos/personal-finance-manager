package com.camilagksantos.finance.integration.persistence;

import com.camilagksantos.finance.adapters.outbound.persistence.adapter.AccountPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.AccountJpaRepository;
import com.camilagksantos.finance.domain.model.Account;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    private Account account;

    @BeforeEach
    void setUp() {
        accountJpaRepository.deleteAll();
        account = new Account(
                UUID.randomUUID(),
                1L,
                "Conta Corrente",
                Account.AccountType.CHECKING,
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldSaveAccount() {
        Account result = accountPersistenceAdapter.save(account);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(account.id());
        assertThat(result.name()).isEqualTo("Conta Corrente");
        assertThat(result.balance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
    }

    @Test
    void shouldFindAccountById() {
        accountPersistenceAdapter.save(account);

        Optional<Account> result = accountPersistenceAdapter.findById(account.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(account.id());
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFound() {
        Optional<Account> result = accountPersistenceAdapter.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAllAccountsByUserId() {
        accountPersistenceAdapter.save(account);

        List<Account> result = accountPersistenceAdapter.findAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteAccountById() {
        accountPersistenceAdapter.save(account);

        accountPersistenceAdapter.deleteById(account.id());

        Optional<Account> result = accountPersistenceAdapter.findById(account.id());
        assertThat(result).isEmpty();
    }
}