package com.camilagksantos.finance.application.mapper;

import com.camilagksantos.finance.application.dto.response.UserResponse;
import com.camilagksantos.finance.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);
}
