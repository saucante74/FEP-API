package com.glg204.fep.api.DashboardAPI;

import com.glg204.fep.application.DashboardApplication.user.UserDashboardService;
import com.glg204.fep.application.DashboardApplication.user.UserDashboardStatsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Unit tests
class UserDashboardControllerStandaloneTest {

    private MockMvc mockMvc;
    private UserDashboardService dashboardService;

    @BeforeEach
    void setup() {
        dashboardService = Mockito.mock(UserDashboardService.class);
        UserDashboardController controller = new UserDashboardController(dashboardService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private UserDashboardStatsDTO getSampleDashboard() {
        return UserDashboardStatsDTO.builder()
                .totalAmount(10000)
                .amountLabel("Total prêté")
                .totalInterests(500)
                .interestsLabel("Intérêts gagnés")
                .activeLoansAmount(8000)
                .totalLoansCount(5)
                .totalRefundsMade(2)
                .totalRefundsReceived(3)
                .loansInProgress(2)
                .lateRefundsCount(1)
                .upcomingRefunds(List.of(
                        UserDashboardStatsDTO.UpcomingRefundDTO.builder()
                                .dueDate(LocalDateTime.of(2025, 9, 15, 10, 0))
                                .amount(300)
                                .status("PENDING")
                                .counterparty("borrower@example.com")
                                .build()
                ))
                .build();
    }

    @Test
    void testGetUserDashboard() throws Exception {
        UserDashboardStatsDTO dto = getSampleDashboard();

        Mockito.when(dashboardService.getUserDashboardStats()).thenReturn(dto);

        mockMvc.perform(get("/api/user/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(10000))
                .andExpect(jsonPath("$.amountLabel").value("Total prêté"))
                .andExpect(jsonPath("$.upcomingRefunds[0].amount").value(300))
                .andExpect(jsonPath("$.upcomingRefunds[0].counterparty").value("borrower@example.com"));
    }
}
