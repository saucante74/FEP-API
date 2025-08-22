package com.glg204.fep.application.DashboardApplication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemStatsDTO {
    private int alertsCount;
    private String lastUpdate;
}
