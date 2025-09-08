package com.glg204.fep.domain.LoanDomain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanDomainServiceTest {

    LoanDomainService service = new LoanDomainService();

    @Test
    void testCalculateTotalInterest() {
        Loan loan = Loan.builder()
                .amount(BigDecimal.valueOf(10000))
                .interestRate(6.0)
                .durationInMonths(12)
                .build();

        BigDecimal interest = service.calculateTotalInterest(loan);
        assertEquals(0, interest.compareTo(BigDecimal.valueOf(600.0)));
    }

    @Test
    void testCalculateAverageInterestRate() {
        List<Loan> loans = List.of(
                Loan.builder().interestRate(5.0).build(),
                Loan.builder().interestRate(7.0).build()
        );

        double avg = service.calculateAverageInterestRate(loans);
        assertEquals(6.0, avg);
    }

    @Test
    void testCalculateAverageLoanDuration() {
        List<Loan> loans = List.of(
                Loan.builder().durationInMonths(12).build(),
                Loan.builder().durationInMonths(24).build()
        );

        int avg = service.calculateAverageLoanDuration(loans);
        assertEquals(18, avg);
    }

    @Test
    void testCalculateDefaultRate() {
        List<Loan> loans = List.of(
                Loan.builder().status(LoanStatus.PENDING).build(),
                Loan.builder().status(LoanStatus.VALIDATED).build(),
                Loan.builder().status(LoanStatus.PENDING).build()
        );

        double rate = service.calculateDefaultRate(loans);
        assertEquals(2.0 / 3.0, rate);
    }

    @Test
    void testGenerateReferenceFormat() {
        String ref = LoanDomainService.generateReference();
        assertTrue(ref.startsWith("LN-"));
        assertTrue(ref.matches("LN-\\d{8}-\\d{9}\\-[A-Z0-9]{4}"));
    }
}
