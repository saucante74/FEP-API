package com.glg204.fep.api.StatisticsAPI;

import com.glg204.fep.application.StatisticsApplication.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Unit tests
class StatisticsControllerStandaloneTest {

    private MockMvc mockMvc;
    private StatisticsService statisticsService;

    @BeforeEach
    void setup() {
        statisticsService = Mockito.mock(StatisticsService.class);
        StatisticsController statisticsController = new StatisticsController(statisticsService);
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    void testGetGlobalStats() throws Exception {
        GlobalStatsDTO dto = GlobalStatsDTO.builder()
                .totalFinanced(100000)
                .totalRevenue(5000)
                .totalCustomers(200)
                .newCustomers(20)
                .loansInProgress(10)
                .loansRepaid(5)
                .refundsInProgress(15)
                .build();

        Mockito.when(statisticsService.getGlobalStats()).thenReturn(dto);

        mockMvc.perform(get("/api/statistics/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFinanced").value(100000))
                .andExpect(jsonPath("$.totalRevenue").value(5000));
    }

    @Test
    void testGetFinancialStats() throws Exception {
        FinancialStatsDTO dto = FinancialStatsDTO.builder()
                .averageInterestRate(5.5)
                .defaultRate(0.02)
                .averageLoanDuration(12)
                .build();

        Mockito.when(statisticsService.getFinancialStats()).thenReturn(dto);

        mockMvc.perform(get("/api/statistics/financial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageInterestRate").value(5.5))
                .andExpect(jsonPath("$.defaultRate").value(0.02));
    }

    @Test
    void testGetRefundStats() throws Exception {
        RefundStatsDTO.MonthlyRefund monthlyRefund = RefundStatsDTO.MonthlyRefund.builder()
                .month("2025-09")
                .refundedAmount(1000)
                .expectedAmount(1200)
                .lateAmount(100)
                .unpaidAmount(100)
                .build();

        RefundStatsDTO dto = RefundStatsDTO.builder()
                .monthlyRefunds(List.of(monthlyRefund))
                .pendingRefundsAmount(500)
                .expectedRevenueNextMonth(200)
                .upcomingRefunds(List.of())
                .lateRefunds(List.of())
                .build();

        Mockito.when(statisticsService.getRefundStats()).thenReturn(dto);

        mockMvc.perform(get("/api/statistics/refunds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyRefunds[0].month").value("2025-09"))
                .andExpect(jsonPath("$.monthlyRefunds[0].refundedAmount").value(1000))
                .andExpect(jsonPath("$.pendingRefundsAmount").value(500))
                .andExpect(jsonPath("$.expectedRevenueNextMonth").value(200));
    }

}
