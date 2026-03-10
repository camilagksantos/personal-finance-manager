package com.camilagksantos.finance.integration.persistence;

import com.camilagksantos.finance.adapters.outbound.persistence.adapter.AccountPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.adapter.CategoryPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.adapter.TransactionPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.AccountJpaRepository;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.CategoryJpaRepository;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.TransactionJpaRepository;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.model.Category;
import com.camilagksantos.finance.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransactionPersistenceAdapterTest {

    @Autowired
    private TransactionPersistenceAdapter transactionPersistenceAdapter;

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    @Autowired
    private CategoryPersistenceAdapter categoryPersistenceAdapter;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionJpaRepository.deleteAll();
        accountJpaRepository.deleteAll();
        categoryJpaRepository.deleteAll();

        account = accountPersistenceAdapter.save(new Account(
                UUID.randomUUID(), 1L, "Conta Poupanca",
                Account.AccountType.CHECKING, BigDecimal.valueOf(1000),
                LocalDateTime.now(), LocalDateTime.now()
        ));

        Category category = categoryPersistenceAdapter.save(new Category(
                UUID.randomUUID(), 1L, "Alimentacao",
                Category.CategoryType.EXPENSE,
                LocalDateTime.now(), LocalDateTime.now()
        ));

        transaction = new Transaction(
                UUID.randomUUID(),
                account.id(),
                category.id(),
                BigDecimal.valueOf(50),
                Transaction.TransactionType.EXPENSE,
                "Supermercado",
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldSaveTransaction() {
        Transaction result = transactionPersistenceAdapter.save(transaction);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(transaction.id());
        assertThat(result.description()).isEqualTo("Supermercado");
    }

    @Test
    void shouldFindTransactionById() {
        transactionPersistenceAdapter.save(transaction);

        Optional<Transaction> result = transactionPersistenceAdapter.findById(transaction.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(transaction.id());
    }

    @Test
    void shouldReturnEmptyWhenTransactionNotFound() {
        Optional<Transaction> result = transactionPersistenceAdapter.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAllTransactionsByAccountId() {
        transactionPersistenceAdapter.save(transaction);

        List<Transaction> result = transactionPersistenceAdapter.findAllByAccountId(account.id());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().accountId()).isEqualTo(account.id());
    }

    @Test
    void shouldFindAllTransactionsByAccountIdAndDateBetween() {
        transactionPersistenceAdapter.save(transaction);

        List<Transaction> result = transactionPersistenceAdapter.findAllByAccountIdAndDateBetween(
                account.id(),
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        );

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldDeleteTransactionById() {
        transactionPersistenceAdapter.save(transaction);

        transactionPersistenceAdapter.deleteById(transaction.id());

        Optional<Transaction> result = transactionPersistenceAdapter.findById(transaction.id());
        assertThat(result).isEmpty();
    }
}