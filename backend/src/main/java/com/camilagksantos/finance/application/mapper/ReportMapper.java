package com.camilagksantos.finance.application.mapper;

import com.camilagksantos.finance.application.dto.request.ReportRequest;
import com.camilagksantos.finance.application.dto.response.ReportResponse;
import com.camilagksantos.finance.domain.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "generatedAt", ignore = true)
    Report toDomain(ReportRequest request);

    ReportResponse toResponse(Report report);
}
