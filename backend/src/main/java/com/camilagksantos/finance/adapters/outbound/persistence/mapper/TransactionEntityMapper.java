package com.camilagksantos.finance.adapters.outbound.persistence.mapper;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.TransactionEntity;
import com.camilagksantos.finance.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper {

    @Mapping(target = "account.id", source = "accountId")
    @Mapping(target = "category.id", source = "categoryId")
    TransactionEntity toEntity(Transaction transaction);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "categoryId", source = "category.id")
    Transaction toDomain(TransactionEntity entity);
}
