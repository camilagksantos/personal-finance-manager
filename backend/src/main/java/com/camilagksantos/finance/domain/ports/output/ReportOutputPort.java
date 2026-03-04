package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.Report;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportOutputPort {

    Report save(Report report);

    Optional<Report> findById(UUID id);

    List<Report> findAllByUserId(Long userId);

    void deleteById(UUID id);
}
