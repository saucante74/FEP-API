package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardStatsDTO getDashboard() {
        return dashboardService.getDashboardStats();
    }

    @GetMapping("/stats/global")
    public GlobalStatsDTO getGlobalStats() {
        return dashboardService.getGlobalStats();
    }

    @GetMapping("/stats/financial")
    public FinancialStatsDTO getFinancialStats() {
        return dashboardService.getFinancialStats();
    }

    @GetMapping("/stats/refunds")
    public RefundStatsDTO getRefundStats() {
        return dashboardService.getRefundStats();
    }

    @GetMapping("/stats/system")
    public SystemStatsDTO getSystemStats() {
        return dashboardService.getSystemStats();
    }
}

