package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.admin.AdminDashboardService;
import com.glg204.fep.application.DashboardApplication.admin.AdminDashboardStatsDTO;
import com.glg204.fep.application.StatisticsApplication.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminDashboardControllerStandaloneTest {

    private MockMvc mockMvc;
    private AdminDashboardService dashboardService;

    @BeforeEach
    void setup() {
        dashboardService = Mockito.mock(AdminDashboardService.class);
        AdminDashboardController controller = new AdminDashboardController(dashboardService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetAdminDashboard() throws Exception {
        AdminDashboardStatsDTO dto = AdminDashboardStatsDTO.builder()
                .global(GlobalStatsDTO.builder().totalCustomers(100).build())
                .financial(FinancialStatsDTO.builder().averageInterestRate(5.5).build())
                .refunds(RefundStatsDTO.builder().pendingRefundsAmount(500).build())
                .system(SystemStatsDTO.builder().alertsCount(3).lastUpdate("2025-09-09T12:00:00").build())
                .build();

        Mockito.when(dashboardService.getAdminDashboardStats()).thenReturn(dto);

        mockMvc.perform(get("/api/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.global.totalCustomers").value(100))
                .andExpect(jsonPath("$.financial.averageInterestRate").value(5.5))
                .andExpect(jsonPath("$.refunds.pendingRefundsAmount").value(500))
                .andExpect(jsonPath("$.system.alertsCount").value(3));
    }
}
