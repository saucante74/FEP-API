package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportServiceTest {

    private ReportRepository reportRepository;
    private UserRepository userRepository;
    private ReportNotificationService reportNotificationService;
    private ReportService reportService;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        userRepository = mock(UserRepository.class);
        reportNotificationService = mock(ReportNotificationService.class);
        reportService = new ReportService(reportRepository, userRepository, reportNotificationService);
    }

    @Test
    void shouldCreateReportSuccessfully() {
        User reporter = new User();
        reporter.setEmail("reporter@example.com");

        User reportedUser = new User();
        reportedUser.setEmail("reported@example.com");

        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReportedUserId(2L);
        dto.setReason(ReportReason.SPAM);

        when(userRepository.findById(2L)).thenReturn(Optional.of(reportedUser));

        Report savedReport = Report.builder()
                .id(1L)
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(ReportReason.SPAM)
                .reportDate(LocalDateTime.now())
                .isOpen(true)
                .build();

        when(reportRepository.save(any(Report.class))).thenReturn(savedReport);

        ReportResponseDTO response = reportService.createReport(dto, reporter);

        assertEquals(ReportReason.SPAM, response.getReason());
        assertEquals("reporter@example.com", response.getReporterEmail());
        assertEquals("reported@example.com", response.getReportedUserEmail());

        verify(reportNotificationService).sendReportReceivedNotification(any());
        verify(reportNotificationService).sendReportEscalationNotification(any());
    }


    @Test
    void shouldThrowExceptionWhenReportedUserNotFound() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReportedUserId(99L);
        dto.setReason(ReportReason.SPAM);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> reportService.createReport(dto, new User()));
    }

    @Test
    void shouldReturnAllReports() {
        Report report = Report.builder()
                .id(1L)
                .reason(ReportReason.ABUSE)
                .reporter(new User())
                .reportedUser(new User())
                .reportDate(LocalDateTime.now())
                .isOpen(true)
                .build();

        when(reportRepository.findAll()).thenReturn(List.of(report));

        List<ReportResponseDTO> reports = reportService.getAllReports();

        assertEquals(1, reports.size());
        assertEquals(ReportReason.ABUSE, reports.get(0).getReason());
    }

    @Test
    void shouldReturnReportsByUser() {
        User user = new User();

        Report report = Report.builder()
                .reporter(user)
                .reportedUser(user)
                .reason(ReportReason.SPAM)
                .reportDate(LocalDateTime.now())
                .isOpen(true)
                .build();

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = mock(SecurityContext.class);
            Authentication auth = mock(Authentication.class);
            when(SecurityContextHolder.getContext()).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(user);

            when(reportRepository.findByReporterOrReportedUser(user, user)).thenReturn(List.of(report));

            List<ReportResponseDTO> result = reportService.getReportsByUser();

            assertEquals(1, result.size());
            assertEquals(ReportReason.SPAM, result.get(0).getReason());
        }
    }

    @Test
    void shouldDeleteReportSuccessfully() {
        reportService.deleteReport(1L);
        verify(reportRepository).deleteById(1L);
    }
}
