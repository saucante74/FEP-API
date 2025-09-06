package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportNotificationService reportNotificationService;

    public ReportResponseDTO createReport(ReportRequestDTO dto, User reporter) {
        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        Report report = Report.builder()
                .reason(ReportReason.valueOf(String.valueOf(dto.getReason())))
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reportDate(LocalDateTime.now())
                .isOpen(true)
                .build();

        reportNotificationService.sendReportReceivedNotification(report);
        reportNotificationService.sendReportEscalationNotification(report);

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


    public List<ReportResponseDTO> getReportsByUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Report> reports = reportRepository.findByReporterOrReportedUser(user, user);

        return reports.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public ReportResponseDTO updateReport(Long id, ReportRequestDTO dto) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        User reportedUser = userRepository.findById(dto.getReportedUserId())
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        report.setReportedUser(reportedUser);
        report.setReason(ReportReason.valueOf(String.valueOf(dto.getReason())));

        return toDto(reportRepository.save(report));
    }

    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    private ReportResponseDTO toDto(Report report) {
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reason(report.getReason())
                .reporterEmail(report.getReporter().getEmail())
                .reportedUserEmail(report.getReportedUser().getEmail())
                .reportDate(report.getReportDate())
                .open(report.isOpen())
                .build();
    }
}
