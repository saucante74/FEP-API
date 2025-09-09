package com.glg204.fep.domain.LoanDomain;

import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void testLoanCreation() {
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

        assertEquals("LN-20250908-0001", loan.getReference());
        assertEquals(BigDecimal.valueOf(10000), loan.getAmount());
        assertEquals(5.0, loan.getInterestRate());
        assertEquals(12, loan.getDurationInMonths());
        assertEquals(LoanStatus.VALIDATED, loan.getStatus());
        assertEquals("lender@example.com", loan.getLender().getEmail());
        assertEquals("borrower@example.com", loan.getBorrower().getEmail());
    }
}
