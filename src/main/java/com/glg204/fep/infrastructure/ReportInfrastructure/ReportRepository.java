package com.glg204.fep.infrastructure.ReportInfrastructure;

import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.UserDomain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReporterOrReportedUser(User reporter, User reportedUser);
}
