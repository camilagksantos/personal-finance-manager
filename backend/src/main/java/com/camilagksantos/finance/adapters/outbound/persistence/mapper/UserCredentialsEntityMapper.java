package com.camilagksantos.finance.adapters.outbound.persistence.mapper;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.UserCredentialsEntity;
import com.camilagksantos.finance.domain.model.UserCredentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialsEntityMapper {

    UserCredentialsEntity toEntity(UserCredentials userCredentials);

    UserCredentials toDomain(UserCredentialsEntity entity);
}