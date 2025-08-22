package com.glg204.fep.application.DashboardApplication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalStatsDTO {
    private int totalFinanced;
    private int totalRevenue;
    private int totalCustomers;
    private int newCustomers;
    private int loansInProgress;
    private int loansRepaid;
    private int refundsInProgress;
}