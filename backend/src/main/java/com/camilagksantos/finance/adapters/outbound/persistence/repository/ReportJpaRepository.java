package com.camilagksantos.finance.adapters.outbound.persistence.repository;

import com.camilagksantos.finance.adapters.outbound.persistence.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReportJpaRepository extends JpaRepository<ReportEntity, UUID> {

    List<ReportEntity> findAllByUserId(Long userId);
}
