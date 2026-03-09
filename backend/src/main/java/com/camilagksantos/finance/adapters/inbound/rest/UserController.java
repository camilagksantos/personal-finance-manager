package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.response.UserResponse;
import com.camilagksantos.finance.application.mapper.UserMapper;
import com.camilagksantos.finance.domain.ports.input.UserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    public UserController(UserUseCase userUseCase, UserMapper userMapper) {
        this.userUseCase = userUseCase;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userMapper.toResponse(userUseCase.getById(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
                userUseCase.getAll()
                        .stream()
                        .map(userMapper::toResponse)
                        .toList()
        );
    }
}
