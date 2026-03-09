package com.camilagksantos.finance.unit.domain.service;

import com.camilagksantos.finance.domain.exception.TransactionNotFoundException;
import com.camilagksantos.finance.domain.model.Transaction;
import com.camilagksantos.finance.domain.ports.output.TransactionOutputPort;
import com.camilagksantos.finance.domain.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionOutputPort transactionOutputPort;

    @InjectMocks
    private TransactionService transactionService;

    private UUID transactionId;
    private UUID accountId;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transactionId = UUID.randomUUID();
        accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        transaction = new Transaction(
                transactionId,
                accountId,
                categoryId,
                BigDecimal.valueOf(50),
                Transaction.TransactionType.EXPENSE,
                "Supermercado",
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldCreateTransaction() {
        when(transactionOutputPort.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.create(transaction);

        assertThat(result).isNotNull();
        assertThat(result.description()).isEqualTo("Supermercado");
        verify(transactionOutputPort, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldGetTransactionById() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getById(transactionId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(transactionId);
        verify(transactionOutputPort, times(1)).findById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getById(transactionId))
                .isInstanceOf(TransactionNotFoundException.class);
        verify(transactionOutputPort, times(1)).findById(transactionId);
    }

    @Test
    void shouldGetAllTransactionsByAccountId() {
        when(transactionOutputPort.findAllByAccountId(accountId)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getAllByAccountId(accountId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().accountId()).isEqualTo(accountId);
        verify(transactionOutputPort, times(1)).findAllByAccountId(accountId);
    }

    @Test
    void shouldGetAllTransactionsByAccountIdAndDateBetween() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        when(transactionOutputPort.findAllByAccountIdAndDateBetween(accountId, start, end))
                .thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getAllByAccountIdAndDateBetween(accountId, start, end);

        assertThat(result).hasSize(1);
        verify(transactionOutputPort, times(1)).findAllByAccountIdAndDateBetween(accountId, start, end);
    }

    @Test
    void shouldUpdateTransaction() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionOutputPort.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.update(transactionId, transaction);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(transactionId);
        verify(transactionOutputPort, times(1)).findById(transactionId);
        verify(transactionOutputPort, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTransaction() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.update(transactionId, transaction))
                .isInstanceOf(TransactionNotFoundException.class);
        verify(transactionOutputPort, never()).save(any(Transaction.class));
    }

    @Test
    void shouldDeleteTransaction() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.delete(transactionId);

        verify(transactionOutputPort, times(1)).deleteById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTransaction() {
        when(transactionOutputPort.findById(transactionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.delete(transactionId))
                .isInstanceOf(TransactionNotFoundException.class);
        verify(transactionOutputPort, never()).deleteById(any());
    }
}