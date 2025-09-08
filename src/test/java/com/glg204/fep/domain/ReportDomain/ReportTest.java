package com.glg204.fep.domain.ReportDomain;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    @Test
    void testReportCreation() {
        User reporter = User.builder()
                .email("reporter@example.com")
                .role(UserRole.BORROWER)
                .status(UserStatus.VALIDATED)
                .build();

        User reportedUser = User.builder()
                .email("reported@example.com")
                .role(UserRole.LENDER)
                .status(UserStatus.VALIDATED)
                .build();

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(ReportReason.FRAUD)
                .isOpen(true)
                .reportDate(LocalDateTime.of(2025, 9, 8, 14, 30))
                .build();

        assertEquals("reporter@example.com", report.getReporter().getEmail());
        assertEquals("reported@example.com", report.getReportedUser().getEmail());
        assertEquals(ReportReason.FRAUD, report.getReason());
        assertTrue(report.isOpen());
        assertEquals(LocalDateTime.of(2025, 9, 8, 14, 30), report.getReportDate());
    }


    @Test
    void testEnumValues() {
        assertEquals("FRAUD", ReportReason.FRAUD.name());
        assertEquals("ABUSE", ReportReason.ABUSE.name());
        assertEquals("SPAM", ReportReason.SPAM.name());
        assertEquals("OTHER", ReportReason.OTHER.name());

        assertEquals(4, ReportReason.values().length);
    }

}
