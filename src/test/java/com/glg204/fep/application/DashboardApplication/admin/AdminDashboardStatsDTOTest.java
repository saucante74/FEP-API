package com.glg204.fep.application.DashboardApplication.admin;

import com.glg204.fep.application.StatisticsApplication.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdminDashboardStatsDTOTest {

    @Test
    void shouldBuildAdminDashboardStatsDTOCorrectly() {
        GlobalStatsDTO globalStats = GlobalStatsDTO.builder()
                .totalFinanced(10000)
                .totalRevenue(500)
                .totalCustomers(200)
                .newCustomers(20)
                .loansInProgress(15)
                .loansRepaid(50)
                .refundsInProgress(5)
                .build();

        FinancialStatsDTO financialStats = FinancialStatsDTO.builder()
                .averageInterestRate(4.5)
                .defaultRate(1.2)
                .averageLoanDuration(12)
                .build();

        RefundStatsDTO refundStats = RefundStatsDTO.builder()
                .pendingRefundsAmount(300)
                .expectedRevenueNextMonth(1000)
                .monthlyRefunds(List.of())
                .upcomingRefunds(List.of())
                .lateRefunds(List.of())
                .build();

        SystemStatsDTO systemStats = SystemStatsDTO.builder()
                .alertsCount(3)
                .lastUpdate("2025-09-09")
                .build();

        AdminDashboardStatsDTO statsDTO = AdminDashboardStatsDTO.builder()
                .global(globalStats)
                .financial(financialStats)
                .refunds(refundStats)
                .system(systemStats)
                .build();

        assertEquals(globalStats, statsDTO.getGlobal());
        assertEquals(financialStats, statsDTO.getFinancial());
        assertEquals(refundStats, statsDTO.getRefunds());
        assertEquals(systemStats, statsDTO.getSystem());
    }
}
