package com.glg204.fep.application.StatisticsApplication;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RefundStatsDTO {
    private List<MonthlyRefund> monthlyRefunds;
    private int pendingRefundsAmount;
    private int expectedRevenueNextMonth;
    private List<UpcomingRefund> upcomingRefunds;
    private List<LateRefund> lateRefunds;

    @Data @Builder
    public static class MonthlyRefund {
        private String month;
        private int refundedAmount;
        private int expectedAmount;
        private int lateAmount;
        private int unpaidAmount;
    }

    @Data @Builder
    public static class UpcomingRefund {
        private String dueDate;
        private int amount;
        private String borrower;
    }

    @Data @Builder
    public static class LateRefund {
        private int refundId;
        private int daysLate;
        private int amount;
        private String borrower;
    }
}
