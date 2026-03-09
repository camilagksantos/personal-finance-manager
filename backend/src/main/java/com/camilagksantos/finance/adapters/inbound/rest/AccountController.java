package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.request.AccountRequest;
import com.camilagksantos.finance.application.dto.response.AccountResponse;
import com.camilagksantos.finance.application.mapper.AccountMapper;
import com.camilagksantos.finance.domain.ports.input.AccountUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountUseCase accountUseCase;
    private final AccountMapper accountMapper;

    public AccountController(AccountUseCase accountUseCase, AccountMapper accountMapper) {
        this.accountUseCase = accountUseCase;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                accountMapper.toResponse(accountUseCase.getById(id))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                accountUseCase.getAllByUserId(userId)
                        .stream()
                        .map(accountMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountMapper.toResponse(
                        accountUseCase.create(accountMapper.toDomain(request))
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable UUID id,
                                                  @Valid @RequestBody AccountRequest request) {
        return ResponseEntity.ok(
                accountMapper.toResponse(
                        accountUseCase.update(id, accountMapper.toDomain(request))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountResponse> delete(@PathVariable UUID id){
        accountUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
