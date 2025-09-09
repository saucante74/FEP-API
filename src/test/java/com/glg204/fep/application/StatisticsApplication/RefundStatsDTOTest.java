package com.glg204.fep.application.StatisticsApplication;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Mappingg Tests
public class RefundStatsDTOTest {

    @Test
    void shouldBuildRefundStatsDTOWithDTOs() {
        RefundStatsDTO.MonthlyRefund monthlyRefund = RefundStatsDTO.MonthlyRefund.builder()
                .month("2025-09")
                .refundedAmount(500)
                .expectedAmount(600)
                .lateAmount(50)
                .unpaidAmount(50)
                .build();

        RefundStatsDTO.UpcomingRefund upcomingRefund = RefundStatsDTO.UpcomingRefund.builder()
                .dueDate("2025-09-15")
                .amount(300)
                .borrower("borrower@example.com")
                .build();

        RefundStatsDTO.LateRefund lateRefund = RefundStatsDTO.LateRefund.builder()
                .refundId(1)
                .daysLate(5)
                .amount(200)
                .borrower("borrower@example.com")
                .build();

        RefundStatsDTO dto = RefundStatsDTO.builder()
                .monthlyRefunds(List.of(monthlyRefund))
                .pendingRefundsAmount(1000)
                .expectedRevenueNextMonth(1500)
                .upcomingRefunds(List.of(upcomingRefund))
                .lateRefunds(List.of(lateRefund))
                .build();

        assertEquals(1000, dto.getPendingRefundsAmount());
        assertEquals("2025-09", dto.getMonthlyRefunds().get(0).getMonth());
        assertEquals("borrower@example.com", dto.getUpcomingRefunds().get(0).getBorrower());
        assertEquals(5, dto.getLateRefunds().get(0).getDaysLate());
    }
}
