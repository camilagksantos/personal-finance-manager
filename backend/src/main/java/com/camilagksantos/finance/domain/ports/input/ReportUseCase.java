package com.camilagksantos.finance.domain.ports.input;

import com.camilagksantos.finance.domain.model.Report;

import java.util.List;
import java.util.UUID;

public interface ReportUseCase {

    Report generate(Report report);

    Report getById(UUID id);

    List<Report> getAllByUserId(Long userId);

    void delete(UUID id);
}
