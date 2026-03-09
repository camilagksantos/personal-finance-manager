package com.camilagksantos.finance.unit.application.mapper;

import com.camilagksantos.finance.application.dto.request.TransactionRequest;
import com.camilagksantos.finance.application.dto.response.TransactionResponse;
import com.camilagksantos.finance.application.mapper.TransactionMapperImpl;
import com.camilagksantos.finance.domain.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {

    @InjectMocks
    private TransactionMapperImpl mapper;

    @Test
    void shouldMapRequestToDomain() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        TransactionRequest request = new TransactionRequest(
                accountId,
                categoryId,
                BigDecimal.valueOf(50),
                Transaction.TransactionType.EXPENSE,
                "Supermercado",
                LocalDate.now()
        );

        Transaction result = mapper.toDomain(request);

        assertThat(result).isNotNull();
        assertThat(result.accountId()).isEqualTo(accountId);
        assertThat(result.categoryId()).isEqualTo(categoryId);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.type()).isEqualTo(Transaction.TransactionType.EXPENSE);
        assertThat(result.description()).isEqualTo("Supermercado");
        assertThat(result.date()).isEqualTo(LocalDate.now());
        assertThat(result.id()).isNull();
        assertThat(result.createdAt()).isNull();
        assertThat(result.updatedAt()).isNull();
    }

    @Test
    void shouldMapDomainToResponse() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                accountId,
                categoryId,
                BigDecimal.valueOf(50),
                Transaction.TransactionType.EXPENSE,
                "Supermercado",
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        TransactionResponse result = mapper.toResponse(transaction);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(transaction.id());
        assertThat(result.accountId()).isEqualTo(accountId);
        assertThat(result.categoryId()).isEqualTo(categoryId);
        assertThat(result.amount()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.type()).isEqualTo(Transaction.TransactionType.EXPENSE);
        assertThat(result.description()).isEqualTo("Supermercado");
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();
    }
}