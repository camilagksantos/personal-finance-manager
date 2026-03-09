package com.camilagksantos.finance.unit.application.mapper;

import com.camilagksantos.finance.application.dto.request.AccountRequest;
import com.camilagksantos.finance.application.dto.response.AccountResponse;
import com.camilagksantos.finance.application.mapper.AccountMapperImpl;
import com.camilagksantos.finance.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountMapperTest {

    @InjectMocks
    private AccountMapperImpl mapper;

    @Test
    void shouldMapRequestToDomain() {
        AccountRequest request = new AccountRequest(
                1L,
                "Conta Corrente",
                Account.AccountType.CHECKING,
                BigDecimal.valueOf(1000)
        );

        Account result = mapper.toDomain(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Conta Corrente");
        assertThat(result.type()).isEqualTo(Account.AccountType.CHECKING);
        assertThat(result.balance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(result.id()).isNull();
        assertThat(result.createdAt()).isNull();
        assertThat(result.updatedAt()).isNull();
    }

    @Test
    void shouldMapDomainToResponse() {
        Account account = new Account(
                UUID.randomUUID(),
                1L,
                "Conta Corrente",
                Account.AccountType.CHECKING,
                BigDecimal.valueOf(1000),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        AccountResponse result = mapper.toResponse(account);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(account.id());
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Conta Corrente");
        assertThat(result.type()).isEqualTo(Account.AccountType.CHECKING);
        assertThat(result.balance()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();
    }
}