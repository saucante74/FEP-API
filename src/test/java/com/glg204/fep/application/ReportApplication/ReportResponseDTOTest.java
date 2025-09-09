package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.ReportReason;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReportResponseDTOTest {

    @Test
    void shouldBuildReportResponseDTOCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        ReportResponseDTO dto = ReportResponseDTO.builder()
                .id(1L)
                .reason(ReportReason.ABUSE)
                .reporterEmail("reporter@example.com")
                .reportedUserEmail("reported@example.com")
                .reportDate(now)
                .open(true)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals(ReportReason.ABUSE, dto.getReason());
        assertEquals("reporter@example.com", dto.getReporterEmail());
        assertEquals("reported@example.com", dto.getReportedUserEmail());
        assertEquals(now, dto.getReportDate());
        assertTrue(dto.isOpen());
    }
}
