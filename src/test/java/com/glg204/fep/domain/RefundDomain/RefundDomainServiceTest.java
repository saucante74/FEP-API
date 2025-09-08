package com.glg204.fep.domain.RefundDomain;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.UserDomain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RefundDomainServiceTest {

    RefundDomainService service = new RefundDomainService();

    Refund createRefund(double amount, LocalDateTime date, RefundStatus status) {
        return Refund.builder()
                .amount(amount)
                .refundDate(date)
                .status(status)
                .loan(Loan.builder()
                        .borrower(User.builder().username("borrower").build())
                        .build())
                .build();
    }

    @Test
    void testCalculatPendingRefundsAmount() {
        List<Refund> refunds = List.of(
                createRefund(100, LocalDateTime.now(), RefundStatus.PENDING),
                createRefund(200, LocalDateTime.now(), RefundStatus.SUBMITTED),
                createRefund(300, LocalDateTime.now(), RefundStatus.APPROVED)
        );

        int result = service.calculatePendingRefundsAmount(refunds);
        assertEquals(300, result);
    }

    @Test
    void testCalculateExpectedRevenueNextMonth() {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDateTime nextMonthDate = nextMonth.atDay(10).atStartOfDay();

        List<Refund> refunds = List.of(
                createRefund(150, nextMonthDate, RefundStatus.PENDING),
                createRefund(250, nextMonthDate, RefundStatus.CANCELLED)
        );

        int result = service.calculateExpectedRevenueNextMonth(refunds);
        assertEquals(150, result);
    }
}
