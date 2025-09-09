package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.user.UserDashboardService;
import com.glg204.fep.application.DashboardApplication.user.UserDashboardStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/dashboard")
@RequiredArgsConstructor
public class UserDashboardController {

    private final UserDashboardService userDashboardService;

    @GetMapping
    public UserDashboardStatsDTO getDashboard() {
        return userDashboardService.getUserDashboardStats();
    }
}

