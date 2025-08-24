package com.glg204.fep.domain.LoanDomain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoanDomainService {


    private static final String PREFIX = "LN";
    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

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

        String year = now.format(YEAR_FORMATTER);
        String timestamp = now.format(TIMESTAMP_FORMATTER);

        String uniquePart = timestamp.substring(timestamp.length() - 4);

        return String.format("%s-%s-%s", PREFIX, year, uniquePart);
    }

}

