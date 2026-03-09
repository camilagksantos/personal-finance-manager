package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.request.TransactionRequest;
import com.camilagksantos.finance.application.dto.response.TransactionResponse;
import com.camilagksantos.finance.application.mapper.TransactionMapper;
import com.camilagksantos.finance.domain.ports.input.TransactionUseCase;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionUseCase transactionUseCase;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionUseCase transactionUseCase, TransactionMapper transactionMapper) {
        this.transactionUseCase = transactionUseCase;
        this.transactionMapper = transactionMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                transactionMapper.toResponse(transactionUseCase.getById(id))
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAllByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(
                transactionUseCase.getAllByAccountId(accountId)
                        .stream()
                        .map(transactionMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/account/{accountId}/period")
    public ResponseEntity<List<TransactionResponse>> getAllByAccountIdAndDateBetween(
            @PathVariable UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                transactionUseCase.getAllByAccountIdAndDateBetween(accountId, startDate, endDate)
                        .stream()
                        .map(transactionMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.toResponse(
                        transactionUseCase.create(transactionMapper.toDomain(request))
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable UUID id,
                                                      @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(
                transactionMapper.toResponse(
                        transactionUseCase.update(id, transactionMapper.toDomain(request))
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        transactionUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
