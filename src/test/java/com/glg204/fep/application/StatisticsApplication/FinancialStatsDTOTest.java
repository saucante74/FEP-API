package com.glg204.fep.application.StatisticsApplication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//Mapping test
public class FinancialStatsDTOTest {

    @Test
    void shouldBuildFinancialStatsDTOCorrectly() {
        FinancialStatsDTO dto = FinancialStatsDTO.builder()
                .averageInterestRate(5.5)
                .defaultRate(0.02)
                .averageLoanDuration(12)
                .build();

        assertEquals(5.5, dto.getAverageInterestRate());
        assertEquals(0.02, dto.getDefaultRate());
        assertEquals(12, dto.getAverageLoanDuration());
    }
}
