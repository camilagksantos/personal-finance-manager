package com.camilagksantos.finance.adapters.outbound.persistence.mapper;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.ReportEntity;
import com.camilagksantos.finance.domain.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportEntityMapper {

    @Mapping(target = "content", ignore = true)
    ReportEntity toEntity(Report report);

    Report toDomain(ReportEntity entity);
}
