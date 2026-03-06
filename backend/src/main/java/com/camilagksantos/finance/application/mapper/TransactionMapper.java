package com.camilagksantos.finance.application.mapper;

import com.camilagksantos.finance.application.dto.request.TransactionRequest;
import com.camilagksantos.finance.application.dto.response.TransactionResponse;
import com.camilagksantos.finance.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Transaction toDomain(TransactionRequest request);

    TransactionResponse toResponse(Transaction transaction);
}
