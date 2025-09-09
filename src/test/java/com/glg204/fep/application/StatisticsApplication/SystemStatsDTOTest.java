package com.glg204.fep.application.StatisticsApplication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Mapping tests
public class SystemStatsDTOTest {

    @Test
    void shouldBuildSystemStatsDTOCorrectly() {
        String now = "2025-09-09T12:00:00";

        SystemStatsDTO dto = SystemStatsDTO.builder()
                .alertsCount(3)
                .lastUpdate(now)
                .build();

        assertEquals(3, dto.getAlertsCount());
        assertEquals(now, dto.getLastUpdate());
    }
}
