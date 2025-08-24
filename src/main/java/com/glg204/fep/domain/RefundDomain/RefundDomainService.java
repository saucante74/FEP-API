package com.glg204.fep.domain.RefundDomain;

import com.glg204.fep.application.DashboardApplication.RefundStatsDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class RefundDomainService {

    private static int toInt(double v) {
        return (int) Math.round(v);
    }

    public List<RefundStatsDTO.MonthlyRefund> calculateMonthlyRefunds(List<Refund> refunds) {
        Map<YearMonth, List<Refund>> grouped = refunds.stream()
                .collect(Collectors.groupingBy(r -> YearMonth.from(r.getRefundDate().toLocalDate())));

        LocalDate today = LocalDate.now();
        List<RefundStatsDTO.MonthlyRefund> monthly = new ArrayList<>();

        grouped.forEach((month, list) -> {
            int expected = list.stream().mapToInt(r -> toInt(r.getAmount())).sum();

            int refunded = list.stream()
                    .filter(r -> r.getStatus() == RefundStatus.APPROVED || r.getStatus() == RefundStatus.COMPLETED)
                    .mapToInt(r -> toInt(r.getAmount()))
                    .sum();

            int late = list.stream()
                    .filter(r -> r.getRefundDate().toLocalDate().isBefore(today) &&
                            (r.getStatus() == RefundStatus.PENDING || r.getStatus() == RefundStatus.SUBMITTED))
                    .mapToInt(r -> toInt(r.getAmount()))
                    .sum();

            int unpaid = Math.max(0, expected - refunded - late);

            monthly.add(RefundStatsDTO.MonthlyRefund.builder()
                    .month(month.toString())
                    .refundedAmount(refunded)
                    .expectedAmount(expected)
                    .lateAmount(late)
                    .unpaidAmount(unpaid)
                    .build());
        });

        monthly.sort(Comparator.comparing(RefundStatsDTO.MonthlyRefund::getMonth));
        return monthly;
    }

    public List<RefundStatsDTO.UpcomingRefund> findUpcomingRefunds(List<Refund> refunds) {
        LocalDate now = LocalDate.now();
        LocalDate limit = now.plusDays(30);

        return refunds.stream()
                .filter(r -> {
                    LocalDate d = r.getRefundDate().toLocalDate();
                    return (d.isAfter(now) && !d.isAfter(limit)) &&
                            (r.getStatus() == RefundStatus.PENDING || r.getStatus() == RefundStatus.SUBMITTED);
                })
                .map(r -> RefundStatsDTO.UpcomingRefund.builder()
                        .dueDate(r.getRefundDate().toLocalDate().toString())
                        .amount(toInt(r.getAmount()))
                        .borrower(r.getLoan().getBorrower() != null
                                ? r.getLoan().getBorrower().getUsername()
                                : "—")
                        .build())
                .toList();
    }

    public List<RefundStatsDTO.LateRefund> findLateRefunds(List<Refund> refunds) {
        LocalDate now = LocalDate.now();

        return refunds.stream()
                .filter(r -> r.getRefundDate().toLocalDate().isBefore(now) &&
                        (r.getStatus() == RefundStatus.PENDING || r.getStatus() == RefundStatus.SUBMITTED))
                .map(r -> RefundStatsDTO.LateRefund.builder()
                        .refundId(r.getId().intValue())
                        .daysLate((int) java.time.temporal.ChronoUnit.DAYS
                                .between(r.getRefundDate().toLocalDate(), now))
                        .amount(toInt(r.getAmount()))
                        .borrower(r.getLoan().getBorrower() != null
                                ? r.getLoan().getBorrower().getUsername()
                                : "—")
                        .build())
                .toList();
    }

    public int calculatePendingRefundsAmount(List<Refund> refunds) {
        return refunds.stream()
                .filter(r -> r.getStatus() == RefundStatus.PENDING || r.getStatus() == RefundStatus.SUBMITTED)
                .mapToInt(r -> toInt(r.getAmount()))
                .sum();
    }

    public int calculateExpectedRevenueNextMonth(List<Refund> refunds) {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        return refunds.stream()
                .filter(r -> YearMonth.from(r.getRefundDate().toLocalDate()).equals(nextMonth) &&
                        r.getStatus() != RefundStatus.CANCELLED)
                .mapToInt(r -> toInt(r.getAmount()))
                .sum();
    }
}
