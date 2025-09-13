package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final LoanRepository loanRepository;
    private final ReportNotificationService reportNotificationService;

    public ReportResponseDTO createReport(ReportRequestDTO dto, User reporter) {
        User reportedUser = null;
        if (dto.getReportedUserId() != null) {
            reportedUser = userRepository.findById(dto.getReportedUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));
        }

        Loan reportedLoan = null;
        if (dto.getReportedLoanId() != null) {
            reportedLoan = loanRepository.findById(dto.getReportedLoanId())
                    .orElseThrow(() -> new IllegalArgumentException("Reported loan not found"));
        }

        Report report = Report.builder()
                .reason(ReportReason.valueOf(String.valueOf(dto.getReason())))
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reportedLoan(reportedLoan)
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

    public List<ReportResponseDTO> getReportsByReporter() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Report> reports = reportRepository.findByReporter(user);

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
    public ReportResponseDTO patchReport(Long id, ReportPatchRequestDTO dto) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (dto.getReason() != null) {
            report.setReason(dto.getReason());
        }

        if (dto.getIsOpen() != null) {
            report.setOpen(dto.getIsOpen());
        }

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
                .reporterId(String.valueOf(report.getReporter().getId()))
                .reportedUserEmail(report.getReportedUser() != null ? report.getReportedUser().getEmail() : null)
                .reportedUserId(String.valueOf(report.getReportedUser() != null ? report.getReportedUser().getId() : null))
                .reportedLoanReference(report.getReportedLoan() != null ? report.getReportedLoan().getReference() : null)
                .reportedLoanId(String.valueOf(report.getReportedLoan() != null ? report.getReportedLoan().getId() : null))
                .reportDate(report.getReportDate())
                .open(report.isOpen())
                .build();
    }
}
