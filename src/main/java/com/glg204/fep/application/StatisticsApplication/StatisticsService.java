package com.glg204.fep.application.StatisticsApplication;

import com.glg204.fep.domain.LoanDomain.*;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.RefundDomain.RefundDomainService;
import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final LoanRepository loanRepository;
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final LoanDomainService loanDomainService = new LoanDomainService();
    private final RefundDomainService refundDomainService = new RefundDomainService();

    public GlobalStatsDTO getGlobalStats() {
        List<Loan> loans = loanRepository.findAll();

        long newCustomers = userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null &&
                        u.getCreatedAt().isAfter(java.time.LocalDateTime.now().minusMonths(1)))
                .count();

        return GlobalStatsDTO.builder()
                .totalFinanced(loans.stream()
                        .map(Loan::getAmount)
                        .mapToInt(BigDecimal::intValue)
                        .sum())
                .totalRevenue((int) loans.stream()
                        .map(loanDomainService::calculateTotalInterest)
                        .mapToDouble(BigDecimal::doubleValue)
                        .sum())
                .totalCustomers((int) userRepository.count())
                .newCustomers((int) newCustomers)
                .loansInProgress((int) loans.stream()
                        .filter(l -> l.getStatus() == LoanStatus.IN_PROGRESS).count())
                .loansRepaid((int) loans.stream()
                        .filter(l -> l.getStatus() == LoanStatus.REPAID).count())
                .refundsInProgress((int) refundRepository.count())
                .build();
    }

    public FinancialStatsDTO getFinancialStats() {
        List<Loan> loans = loanRepository.findAll();

        return FinancialStatsDTO.builder()
                .averageInterestRate(loanDomainService.calculateAverageInterestRate(loans))
                .defaultRate(loanDomainService.calculateDefaultRate(loans))
                .averageLoanDuration(loanDomainService.calculateAverageLoanDuration(loans))
                .build();
    }

    public RefundStatsDTO getRefundStats() {
        List<Refund> refunds = refundRepository.findAll();

        return RefundStatsDTO.builder()
                .monthlyRefunds(refundDomainService.calculateMonthlyRefunds(refunds))
                .pendingRefundsAmount(refundDomainService.calculatePendingRefundsAmount(refunds))
                .expectedRevenueNextMonth(refundDomainService.calculateExpectedRevenueNextMonth(refunds))
                .upcomingRefunds(refundDomainService.findUpcomingRefunds(refunds))
                .lateRefunds(refundDomainService.findLateRefunds(refunds))
                .build();
    }

    public SystemStatsDTO getSystemStats() {
        return SystemStatsDTO.builder()
                .alertsCount((int) reportRepository.findAll().stream().filter(Report::isOpen).count())
                .lastUpdate(java.time.LocalDateTime.now().toString())
                .build();
    }
}
