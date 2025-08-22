package com.glg204.fep.application.DashboardApplication;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DashboardStatsDTO {
    private GlobalStatsDTO global;
    private FinancialStatsDTO financial;
    private RefundStatsDTO refunds;
    private SystemStatsDTO system;
}

