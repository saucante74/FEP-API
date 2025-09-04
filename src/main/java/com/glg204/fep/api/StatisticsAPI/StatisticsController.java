package com.glg204.fep.api.StatisticsAPI;

import com.glg204.fep.application.StatisticsApplication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/global")
    public GlobalStatsDTO getGlobalStats() {
        return statisticsService.getGlobalStats();
    }

    @GetMapping("/financial")
    public FinancialStatsDTO getFinancialStats() {
        return statisticsService.getFinancialStats();
    }

    @GetMapping("/refunds")
    public RefundStatsDTO getRefundStats() {
        return statisticsService.getRefundStats();
    }

    @GetMapping("/system")
    public SystemStatsDTO getSystemStats() {
        return statisticsService.getSystemStats();
    }
}


