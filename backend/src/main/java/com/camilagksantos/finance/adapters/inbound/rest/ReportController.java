package com.camilagksantos.finance.adapters.inbound.rest;

import com.camilagksantos.finance.application.dto.request.ReportRequest;
import com.camilagksantos.finance.application.dto.response.ReportResponse;
import com.camilagksantos.finance.application.mapper.ReportMapper;
import com.camilagksantos.finance.domain.ports.input.ReportUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportUseCase reportUseCase;
    private final ReportMapper reportMapper;

    public ReportController(ReportUseCase reportUseCase, ReportMapper reportMapper) {
        this.reportUseCase = reportUseCase;
        this.reportMapper = reportMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                reportMapper.toResponse(reportUseCase.getById(id))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportResponse>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                reportUseCase.getAllByUserId(userId)
                        .stream()
                        .map(reportMapper::toResponse)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<ReportResponse> generate(@Valid @RequestBody ReportRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reportMapper.toResponse(
                        reportUseCase.generate(reportMapper.toDomain(request))
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reportUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
