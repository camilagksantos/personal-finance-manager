package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.ReportGenerationException;
import com.camilagksantos.finance.domain.exception.ResourceNotFoundException;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.ports.input.ReportUseCase;
import com.camilagksantos.finance.domain.ports.output.ReportOutputPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService implements ReportUseCase {

    private final ReportOutputPort reportOutputPort;

    public ReportService(ReportOutputPort reportOutputPort) {
        this.reportOutputPort = reportOutputPort;
    }

    @Override
    public Report generate(Report report) {
        try {
            Report newReport = new Report(
                    UUID.randomUUID(),
                    report.userId(),
                    report.title(),
                    report.type(),
                    report.startDate(),
                    report.endDate(),
                    LocalDateTime.now()
            );
            return reportOutputPort.save(newReport);
        } catch (Exception e) {
            throw new ReportGenerationException(e.getMessage());
        }
    }

    @Override
    public Report getById(UUID id) {
        return reportOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    @Override
    public List<Report> getAllByUserId(Long userId) {
        return reportOutputPort.findAllByUserId(userId);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        reportOutputPort.deleteById(id);
    }
}