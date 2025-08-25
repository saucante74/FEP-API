package com.glg204.fep.domain.LoanDomain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class LoanDomainService {


    private static final String PREFIX = "LN";

    public BigDecimal calculateTotalInterest(Loan loan) {
        return loan.getAmount()
                .multiply(BigDecimal.valueOf(loan.getInterestRate() / 100))
                .multiply(BigDecimal.valueOf(loan.getDurationInMonths() / 12.0));
    }

    public double calculateAverageInterestRate(List<Loan> loans) {
        if (loans.isEmpty()) return 0.0;
        return loans.stream()
                .mapToDouble(Loan::getInterestRate)
                .average()
                .orElse(0.0);
    }

    public int calculateAverageLoanDuration(List<Loan> loans) {
        if (loans.isEmpty()) return 0;
        return (int) loans.stream()
                .mapToInt(Loan::getDurationInMonths)
                .average()
                .orElse(0);
    }

    public double calculateDefaultRate(List<Loan> loans) {
        if (loans.isEmpty()) return 0.0;
        long defaults = loans.stream()
                .filter(loan -> loan.getStatus() == LoanStatus.PENDING)
                .count();
        return (double) defaults / loans.size();
    }




    public static String generateReference() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");
        String timestamp = now.format(formatter);
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        return String.format("%s-%s-%s", PREFIX, timestamp, uuidPart);
    }


}

