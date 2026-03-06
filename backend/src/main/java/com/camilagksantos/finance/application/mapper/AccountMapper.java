package com.camilagksantos.finance.application.mapper;

import com.camilagksantos.finance.application.dto.request.AccountRequest;
import com.camilagksantos.finance.application.dto.response.AccountResponse;
import com.camilagksantos.finance.domain.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Account toDomain(AccountRequest request);

    AccountResponse toResponse(Account account);
}
