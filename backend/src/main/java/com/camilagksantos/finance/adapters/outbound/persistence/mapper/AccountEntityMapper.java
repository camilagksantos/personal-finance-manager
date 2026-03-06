package com.camilagksantos.finance.adapters.outbound.persistence.mapper;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.AccountEntity;
import com.camilagksantos.finance.domain.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountEntityMapper {

    AccountEntity toEntity(Account account);

    Account toDomain(AccountEntity entity);
}
