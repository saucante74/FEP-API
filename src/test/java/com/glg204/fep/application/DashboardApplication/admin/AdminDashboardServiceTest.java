package com.glg204.fep.application.DashboardApplication.admin;

import com.glg204.fep.application.StatisticsApplication.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminDashboardServiceTest {

    private StatisticsService statisticsService;
    private AdminDashboardService adminDashboardService;

    @BeforeEach
    void setup() {
        statisticsService = mock(StatisticsService.class);
        adminDashboardService = new AdminDashboardService(statisticsService);
    }

    @Test
    void shouldReturnCorrectAdminStats() {
        GlobalStatsDTO globalStats = GlobalStatsDTO.builder().build();
        FinancialStatsDTO financialStats = FinancialStatsDTO.builder().build();
        RefundStatsDTO refundStats = RefundStatsDTO.builder().build();
        SystemStatsDTO systemStats = SystemStatsDTO.builder().build();

        when(statisticsService.getGlobalStats()).thenReturn(globalStats);
        when(statisticsService.getFinancialStats()).thenReturn(financialStats);
        when(statisticsService.getRefundStats()).thenReturn(refundStats);
        when(statisticsService.getSystemStats()).thenReturn(systemStats);

        AdminDashboardStatsDTO stats = adminDashboardService.getAdminDashboardStats();

        assertEquals(globalStats, stats.getGlobal());
        assertEquals(financialStats, stats.getFinancial());
        assertEquals(refundStats, stats.getRefunds());
        assertEquals(systemStats, stats.getSystem());
    }
}
