package com.camilagksantos.finance.unit.domain.service;

import com.camilagksantos.finance.domain.exception.ResourceNotFoundException;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.ports.output.ReportOutputPort;
import com.camilagksantos.finance.domain.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportOutputPort reportOutputPort;

    @InjectMocks
    private ReportService reportService;

    private UUID reportId;
    private Report report;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        report = new Report(
                reportId,
                1L,
                "Relatório Mensal",
                Report.ReportType.MONTHLY_STATEMENT,
                LocalDateTime.now()
        );
    }

    @Test
    void shouldGenerateReport() {
        when(reportOutputPort.save(any(Report.class))).thenReturn(report);

        Report result = reportService.generate(report);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Relatório Mensal");
        verify(reportOutputPort, times(1)).save(any(Report.class));
    }

    @Test
    void shouldGetReportById() {
        when(reportOutputPort.findById(reportId)).thenReturn(Optional.of(report));

        Report result = reportService.getById(reportId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(reportId);
        verify(reportOutputPort, times(1)).findById(reportId);
    }

    @Test
    void shouldThrowExceptionWhenReportNotFound() {
        when(reportOutputPort.findById(reportId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.getById(reportId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(reportOutputPort, times(1)).findById(reportId);
    }

    @Test
    void shouldGetAllReportsByUserId() {
        when(reportOutputPort.findAllByUserId(1L)).thenReturn(List.of(report));

        List<Report> result = reportService.getAllByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().userId()).isEqualTo(1L);
        verify(reportOutputPort, times(1)).findAllByUserId(1L);
    }

    @Test
    void shouldDeleteReport() {
        when(reportOutputPort.findById(reportId)).thenReturn(Optional.of(report));

        reportService.delete(reportId);

        verify(reportOutputPort, times(1)).deleteById(reportId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentReport() {
        when(reportOutputPort.findById(reportId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.delete(reportId))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(reportOutputPort, never()).deleteById(any());
    }
}