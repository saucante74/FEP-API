package com.glg204.fep.application.DashboardApplication.user;

import com.glg204.fep.application.StatisticsApplication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDashboardService {
    private final StatisticsService statisticsService;

    public UserDashboardStatsDTO getUserDashboardStats() {
        return UserDashboardStatsDTO.builder()
                .global(statisticsService.getGlobalStats())
                .financial(statisticsService.getFinancialStats())
                .refunds(statisticsService.getRefundStats())
                .system(statisticsService.getSystemStats())
                .build();
    }
}
