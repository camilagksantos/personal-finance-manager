package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserOutputPort {

    Optional<User> findById(Long id);

    List<User> findAll();
}
