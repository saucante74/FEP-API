package com.glg204.fep.application.DashboardApplication.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDashboardStatsDTOTest {

    @Test
    void shouldBuildUserDashboardStatsDTOCorrectly() {
        UserDashboardStatsDTO.UpcomingRefundDTO refundDTO = UserDashboardStatsDTO.UpcomingRefundDTO.builder()
                .dueDate(LocalDateTime.of(2025, 9, 10, 12, 0))
                .amount(200)
                .status("EN_ATTENTE")
                .counterparty("John Doe")
                .build();

        UserDashboardStatsDTO statsDTO = UserDashboardStatsDTO.builder()
                .totalAmount(1000)
                .amountLabel("Total prêté")
                .totalInterests(50)
                .interestsLabel("Intérêts gagnés")
                .activeLoansAmount(800)
                .totalLoansCount(5)
                .totalRefundsMade(2)
                .totalRefundsReceived(3)
                .loansInProgress(1)
                .lateRefundsCount(1)
                .upcomingRefunds(List.of(refundDTO))
                .build();

        assertEquals(1000, statsDTO.getTotalAmount());
        assertEquals("Total prêté", statsDTO.getAmountLabel());
        assertEquals(50, statsDTO.getTotalInterests());
        assertEquals("Intérêts gagnés", statsDTO.getInterestsLabel());
        assertEquals(800, statsDTO.getActiveLoansAmount());
        assertEquals(5, statsDTO.getTotalLoansCount());
        assertEquals(2, statsDTO.getTotalRefundsMade());
        assertEquals(3, statsDTO.getTotalRefundsReceived());
        assertEquals(1, statsDTO.getLoansInProgress());
        assertEquals(1, statsDTO.getLateRefundsCount());
        assertEquals(1, statsDTO.getUpcomingRefunds().size());
        assertEquals("John Doe", statsDTO.getUpcomingRefunds().get(0).getCounterparty());
    }
}
