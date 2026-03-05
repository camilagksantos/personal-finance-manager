package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.User;

import java.util.List;

public interface UserUseCase {

    User getById(Long id);

    List<User> getAll();
}
