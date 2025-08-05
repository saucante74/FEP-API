package com.glg204.fep.infrastructure.ReportInfrastructure;

import com.glg204.fep.domain.ReportDomain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> { }
