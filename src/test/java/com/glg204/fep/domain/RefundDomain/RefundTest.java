package com.glg204.fep.domain.RefundDomain;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RefundTest {

    @Test
    void testRefundCreation() {
        User lender = User.builder()
                .email("lender@example.com")
                .role(UserRole.LENDER)
                .status(UserStatus.VALIDATED)
                .build();

        User borrower = User.builder()
                .email("borrower@example.com")
                .role(UserRole.BORROWER)
                .status(UserStatus.VALIDATED)
                .build();

        Loan loan = Loan.builder()
                .reference("LN-20250908-0001")
                .amount(BigDecimal.valueOf(10000))
                .interestRate(5.0)
                .durationInMonths(12)
                .startDate(LocalDate.now())
                .status(LoanStatus.VALIDATED)
                .lender(lender)
                .borrower(borrower)
                .build();

        Refund refund = Refund.builder()
                .amount(500.0)
                .refundDate(LocalDateTime.of(2025, 9, 10, 10, 0))
                .status(RefundStatus.SUBMITTED)
                .loan(loan)
                .build();

        assertEquals(500.0, refund.getAmount());
        assertEquals(LocalDateTime.of(2025, 9, 10, 10, 0), refund.getRefundDate());
        assertEquals(RefundStatus.SUBMITTED, refund.getStatus());
        assertEquals("borrower@example.com", refund.getLoan().getBorrower().getEmail());
    }
}
