package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.UserNotFoundException;
import com.camilagksantos.finance.domain.model.User;
import com.camilagksantos.finance.domain.ports.input.UserUseCase;
import com.camilagksantos.finance.domain.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserUseCase {

    private final UserOutputPort userOutputPort;

    public UserService(UserOutputPort userOutputPort) {
        this.userOutputPort = userOutputPort;
    }

    @Override
    public User getById(Long id) {
        return userOutputPort.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAll() {
        return userOutputPort.findAll();
    }
}