package com.glg204.fep.application.DashboardApplication.admin;

import com.glg204.fep.application.StatisticsApplication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final StatisticsService statisticsService;

    public AdminDashboardStatsDTO getAdminDashboardStats() {
        return AdminDashboardStatsDTO.builder()
                .global(statisticsService.getGlobalStats())
                .financial(statisticsService.getFinancialStats())
                .refunds(statisticsService.getRefundStats())
                .system(statisticsService.getSystemStats())
                .build();
    }
}
