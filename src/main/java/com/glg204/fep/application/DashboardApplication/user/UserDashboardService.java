package com.glg204.fep.application.DashboardApplication.user;

import com.glg204.fep.application.StatisticsApplication.StatisticsService;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.LoanDomain.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDashboardService {

    private final StatisticsService statisticsService;

    public UserDashboardStatsDTO getUserDashboardStats() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Loan> loans = statisticsService.getLoansByUser(user);
        List<Refund> refunds = statisticsService.getRefundsByUser(user);

        boolean isLender = user.getRole() == UserRole.LENDER;

        int totalAmount = isLender
                ? loans.stream()
                .filter(l -> l.getLender().equals(user))
                .map(Loan::getAmount)
                .mapToInt(BigDecimal::intValue)
                .sum()
                : loans.stream()
                .filter(l -> l.getBorrower().equals(user))
                .map(Loan::getAmount)
                .mapToInt(BigDecimal::intValue)
                .sum();

        int totalInterests = isLender
                ? loans.stream().map(statisticsService::calculateInterestEarned).mapToInt(BigDecimal::intValue).sum()
                : loans.stream().map(statisticsService::calculateInterestPaid).mapToInt(BigDecimal::intValue).sum();

        return UserDashboardStatsDTO.builder()
                .totalAmount(totalAmount)
                .amountLabel(isLender ? "Total prêté" : "Total emprunté")
                .totalInterests(totalInterests)
                .interestsLabel(isLender ? "Intérêts gagnés" : "Intérêts payés")
                .activeLoansAmount(statisticsService.calculateActiveLoansAmount(loans))
                .totalLoansCount(loans.size())
                .totalRefundsMade((int) refunds.stream().filter(r -> !isLender).count())
                .totalRefundsReceived((int) refunds.stream().filter(r -> isLender).count())
                .loansInProgress((int) loans.stream()
                        .filter(l -> l.getStatus() == LoanStatus.IN_PROGRESS)
                        .count())
                .lateRefundsCount(statisticsService.countLateRefunds(refunds))
                .upcomingRefunds(statisticsService.mapUpcomingRefunds(user, refunds))
                .build();
    }
}
