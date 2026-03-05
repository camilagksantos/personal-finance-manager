package com.camilagksantos.finance.adapters.outbound.persistence.adapter;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.ReportEntity;
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

    public ReportPersistenceAdapter(ReportJpaRepository reportJpaRepository) {
        this.reportJpaRepository = reportJpaRepository;
    }

    @Override
    public Report save(Report report) {
        ReportEntity entity = toEntity(report);
        ReportEntity saved = reportJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Report> findById(UUID id) {
        return reportJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Report> findAllByUserId(Long userId) {
        return reportJpaRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        reportJpaRepository.deleteById(id);
    }

    private ReportEntity toEntity(Report report) {
        ReportEntity entity = new ReportEntity();
        entity.setId(report.id());
        entity.setUserId(report.userId());
        entity.setTitle(report.title());
        entity.setType(report.type());
        entity.setGeneratedAt(report.generatedAt());
        entity.setContent(null);
        return entity;
    }

    private Report toDomain(ReportEntity entity) {
        return new Report(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getType(),
                entity.getGeneratedAt()
        );
    }
}