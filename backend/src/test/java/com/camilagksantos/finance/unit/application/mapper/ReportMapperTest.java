package com.camilagksantos.finance.unit.application.mapper;

import com.camilagksantos.finance.application.dto.request.ReportRequest;
import com.camilagksantos.finance.application.dto.response.ReportResponse;
import com.camilagksantos.finance.application.mapper.ReportMapperImpl;
import com.camilagksantos.finance.domain.model.Report;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReportMapperTest {

    @InjectMocks
    private ReportMapperImpl mapper;

    @Test
    void shouldMapRequestToDomain() {
        ReportRequest request = new ReportRequest(
                1L,
                "Relatório Mensal",
                Report.ReportType.MONTHLY_STATEMENT,
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );

        Report result = mapper.toDomain(request);

        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Relatório Mensal");
        assertThat(result.type()).isEqualTo(Report.ReportType.MONTHLY_STATEMENT);
        assertThat(result.startDate()).isNotNull();
        assertThat(result.endDate()).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.generatedAt()).isNull();
    }

    @Test
    void shouldMapDomainToResponse() {
        Report report = new Report(
                UUID.randomUUID(),
                1L,
                "Relatório Mensal",
                Report.ReportType.MONTHLY_STATEMENT,
                LocalDate.now().minusMonths(1),
                LocalDate.now(),
                LocalDateTime.now()
        );

        ReportResponse result = mapper.toResponse(report);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(report.id());
        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Relatório Mensal");
        assertThat(result.type()).isEqualTo(Report.ReportType.MONTHLY_STATEMENT);
        assertThat(result.startDate()).isNotNull();
        assertThat(result.endDate()).isNotNull();
        assertThat(result.generatedAt()).isNotNull();
    }
}