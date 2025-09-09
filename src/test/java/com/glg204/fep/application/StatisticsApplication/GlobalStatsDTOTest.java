package com.glg204.fep.application.StatisticsApplication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Mapping tests
public class GlobalStatsDTOTest {

    @Test
    void shouldBuildGlobalStatsDTOCorrectly() {
        GlobalStatsDTO dto = GlobalStatsDTO.builder()
                .totalFinanced(1000)
                .totalRevenue(200)
                .totalCustomers(10)
                .newCustomers(2)
                .loansInProgress(3)
                .loansRepaid(5)
                .refundsInProgress(4)
                .build();

        assertEquals(1000, dto.getTotalFinanced());
        assertEquals(200, dto.getTotalRevenue());
        assertEquals(10, dto.getTotalCustomers());
        assertEquals(2, dto.getNewCustomers());
    }
}
