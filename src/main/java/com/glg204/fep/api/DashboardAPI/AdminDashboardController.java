package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.admin.AdminDashboardService;
import com.glg204.fep.application.DashboardApplication.admin.AdminDashboardStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping
    public AdminDashboardStatsDTO getDashboard() {
        return dashboardService.getAdminDashboardStats();
    }
}

