package com.glg204.fep.application.StatisticsApplication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialStatsDTO {
    private double averageInterestRate;
    private double defaultRate;
    private int averageLoanDuration;
}