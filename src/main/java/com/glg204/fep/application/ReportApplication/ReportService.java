package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .isOpen(true)
                .build();

        return toDto(reportRepository.save(report));
    }

    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ReportResponseDTO getReportById(Long id) {
        return reportRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }

    @Transactional
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO dto) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        report.setReportedUser(reportedUser);
        report.setReason(ReportReason.valueOf(dto.getReason()));

        return toDto(reportRepository.save(report));
    }

    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    private ReportResponseDTO toDto(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reason(report.getReason().name())
                .reporterEmail(report.getReporter().getEmail())
                .reportedUserEmail(report.getReportedUser().getEmail())
                .reportDate(report.getReportDate())
                .status(report.getReason())
                .build();
    }
}
