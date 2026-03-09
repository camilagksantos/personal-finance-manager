package com.camilagksantos.finance.unit.domain.service;

import com.camilagksantos.finance.domain.exception.AccountNotFoundException;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.ports.output.AccountOutputPort;
import com.camilagksantos.finance.domain.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountOutputPort accountOutputPort;

    @InjectMocks
    private AccountService accountService;

    private UUID accountId;
    private Account account;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        account = new Account(
                accountId,
                1L,
                "Conta Corrente",
                Account.AccountType.CHECKING,
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateAccount() {
        when(accountOutputPort.save(any(Account.class))).thenReturn(account);

        Account result = accountService.create(account);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Conta Corrente");
        verify(accountOutputPort, times(1)).save(any(Account.class));
    }

    @Test
    void shouldGetAccountById() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getById(accountId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(accountId);
        verify(accountOutputPort, times(1)).findById(accountId);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getById(accountId))
                .isInstanceOf(AccountNotFoundException.class);
        verify(accountOutputPort, times(1)).findById(accountId);
    }

    @Test
    void shouldGetAllAccountsByUserId() {
        when(accountOutputPort.findAllByUserId(1L)).thenReturn(List.of(account));

        List<Account> result = accountService.getAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
        verify(accountOutputPort, times(1)).findAllByUserId(1L);
    }

    @Test
    void shouldUpdateAccount() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.of(account));
        when(accountOutputPort.save(any(Account.class))).thenReturn(account);

        Account result = accountService.update(accountId, account);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(accountId);
        verify(accountOutputPort, times(1)).findById(accountId);
        verify(accountOutputPort, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentAccount() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.update(accountId, account))
                .isInstanceOf(AccountNotFoundException.class);
        verify(accountOutputPort, never()).save(any(Account.class));
    }

    @Test
    void shouldDeleteAccount() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.of(account));

        accountService.delete(accountId);

        verify(accountOutputPort, times(1)).deleteById(accountId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentAccount() {
        when(accountOutputPort.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.delete(accountId))
                .isInstanceOf(AccountNotFoundException.class);
        verify(accountOutputPort, never()).deleteById(any());
    }
}