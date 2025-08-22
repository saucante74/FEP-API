package com.glg204.fep.application.DashboardApplication;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardStatsDTO {
    private GlobalStats global;
    private FinancialStats financial;
    private RefundStats refunds;
    private SystemStats system;

    @Data
    @Builder
    public static class GlobalStats {
        private int totalFinanced;
        private int totalRevenue;
        private int totalCustomers;
        private int newCustomers;
        private int loansInProgress;
        private int loansRepaid;
        private int refundsInProgress;
    }

    @Data
    @Builder
    public static class FinancialStats {
        private double averageInterestRate;
        private double defaultRate;
        private int averageLoanDuration;
    }

    @Data
    @Builder
    public static class RefundStats {
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

    @Data
    @Builder
    public static class SystemStats {
        private int alertsCount;
        private String lastUpdate;
    }
}

