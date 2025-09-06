package com.glg204.fep.application.DashboardApplication.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserDashboardStatsDTO {

    private int totalAmount;
    private String amountLabel;
    private int totalInterests;
    private String interestsLabel;
    private int activeLoansAmount;
    private int totalLoansCount;
    private int totalRefundsMade;
    private int totalRefundsReceived;
    private int loansInProgress;
    private int lateRefundsCount;
    private List<UpcomingRefundDTO> upcomingRefunds;

    @Data @Builder
    public static class UpcomingRefundDTO {
        private LocalDateTime dueDate;
        private int amount;
        private String status;
        private String counterparty;
    }
}


