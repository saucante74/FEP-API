package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.DashboardService;
import com.glg204.fep.application.DashboardApplication.DashboardStatsDTO;
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
}

