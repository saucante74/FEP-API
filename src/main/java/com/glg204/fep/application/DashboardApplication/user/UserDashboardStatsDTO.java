package com.glg204.fep.application.DashboardApplication.user;

import com.glg204.fep.application.StatisticsApplication.FinancialStatsDTO;
import com.glg204.fep.application.StatisticsApplication.GlobalStatsDTO;
import com.glg204.fep.application.StatisticsApplication.RefundStatsDTO;
import com.glg204.fep.application.StatisticsApplication.SystemStatsDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDashboardStatsDTO {
    private GlobalStatsDTO global;
    private FinancialStatsDTO financial;
    private RefundStatsDTO refunds;
    private SystemStatsDTO system;
}

