package com.camilagksantos.finance.integration.persistence;

import com.camilagksantos.finance.adapters.outbound.persistence.adapter.ReportPersistenceAdapter;
import com.camilagksantos.finance.adapters.outbound.persistence.repository.ReportJpaRepository;
import com.camilagksantos.finance.domain.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReportPersistenceAdapterTest {

    @Autowired
    private ReportPersistenceAdapter reportPersistenceAdapter;

    @Autowired
    private ReportJpaRepository reportJpaRepository;

    private Report report;

    @BeforeEach
    void setUp() {
        reportJpaRepository.deleteAll();
        report = new Report(
                UUID.randomUUID(),
                1L,
                "Relatorio Mensal",
                Report.ReportType.MONTHLY_STATEMENT,
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldSaveReport() {
        Report result = reportPersistenceAdapter.save(report);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(report.id());
        assertThat(result.title()).isEqualTo("Relatorio Mensal");
    }

    @Test
    void shouldFindReportById() {
        reportPersistenceAdapter.save(report);

        Optional<Report> result = reportPersistenceAdapter.findById(report.id());

        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(report.id());
    }

    @Test
    void shouldReturnEmptyWhenReportNotFound() {
        Optional<Report> result = reportPersistenceAdapter.findById(UUID.randomUUID());

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindAllReportsByUserId() {
        reportPersistenceAdapter.save(report);

        List<Report> result = reportPersistenceAdapter.findAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteReportById() {
        reportPersistenceAdapter.save(report);

        reportPersistenceAdapter.deleteById(report.id());

        Optional<Report> result = reportPersistenceAdapter.findById(report.id());
        assertThat(result).isEmpty();
    }
}