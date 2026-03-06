package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.mapper.ReportEntityMapper;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.ReportJpaRepository;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.ports.output.ReportOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReportPersistenceAdapter implements ReportOutputPort {

    private final ReportJpaRepository reportJpaRepository;
    private final ReportEntityMapper reportEntityMapper;

    public ReportPersistenceAdapter(ReportJpaRepository reportJpaRepository, ReportEntityMapper reportEntityMapper) {
        this.reportJpaRepository = reportJpaRepository;
        this.reportEntityMapper = reportEntityMapper;
    }

    @Override
    public Report save(Report report) {
        return reportEntityMapper.toDomain(
                reportJpaRepository.save(reportEntityMapper.toEntity(report))
        );
    }

    @Override
    public Optional<Report> findById(UUID id) {
        return reportJpaRepository.findById(id)
                .map(reportEntityMapper::toDomain);
    }

    @Override
    public List<Report> findAllByUserId(Long userId) {
        return reportJpaRepository.findAllByUserId(userId)
                .stream()
                .map(reportEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        reportJpaRepository.deleteById(id);
    }
}