package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportResponseDTO createReport(ReportRequestDTO dto, User reporter) {
        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        Report report = Report.builder()
                .reason(ReportReason.valueOf(dto.getReason()))
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reportDate(LocalDateTime.now())
                .build();

        Report saved = reportRepository.save(report);

        return ReportResponseDTO.builder()
                .id(saved.getId())
                .reason(String.valueOf(saved.getReason()))
                .reportDate(saved.getReportDate())
                .reporterEmail(saved.getReporter().getEmail())
                .reportedUserEmail(saved.getReportedUser().getEmail())
                .build();
    }

}

