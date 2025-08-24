package com.glg204.fep.application.DashboardApplication;

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
public class DashboardService {

    private final LoanRepository loanRepository;
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final LoanDomainService loanDomainService = new LoanDomainService();

    public DashboardStatsDTO getDashboardStats() {
        List<Loan> loans = loanRepository.findAll();
        List<Refund> refunds = refundRepository.findAll();

        FinancialStatsDTO financial = FinancialStatsDTO.builder()
                .averageInterestRate(loanDomainService.calculateAverageInterestRate(loans))
                .defaultRate(loanDomainService.calculateDefaultRate(loans))
                .averageLoanDuration(loanDomainService.calculateAverageLoanDuration(loans))
                .build();

        GlobalStatsDTO global = GlobalStatsDTO.builder()
                .totalFinanced(loans.stream().mapToInt(l -> l.getAmount().intValue()).sum())
                .totalRevenue((int) loans.stream()
                        .map(loanDomainService::calculateTotalInterest)
                        .mapToDouble(BigDecimal::doubleValue)
                        .sum())
                .totalCustomers((int) userRepository.count())
                .newCustomers(2)
                .loansInProgress((int) loans.stream().filter(l -> l.getStatus() == LoanStatus.IN_PROGRESS).count())
                .loansRepaid((int) loans.stream().filter(l -> l.getStatus() == LoanStatus.REPAID).count())
                .refundsInProgress((int) refundRepository.count())
                .build();

        SystemStatsDTO system = SystemStatsDTO.builder()
                .alertsCount((int) reportRepository.findAll().stream().filter(Report::isOpen).count())
                .lastUpdate(java.time.LocalDateTime.now().toString())
                .build();

        RefundDomainService refundDomainService = new RefundDomainService();

        RefundStatsDTO refundsStats = RefundStatsDTO.builder()
                .monthlyRefunds(refundDomainService.calculateMonthlyRefunds(refunds))
                .pendingRefundsAmount(refundDomainService.calculatePendingRefundsAmount(refunds))
                .expectedRevenueNextMonth(refundDomainService.calculateExpectedRevenueNextMonth(refunds))
                .upcomingRefunds(refundDomainService.findUpcomingRefunds(refunds))
                .lateRefunds(refundDomainService.findLateRefunds(refunds))
                .build();

        return DashboardStatsDTO.builder()
                .global(global)
                .financial(financial)
                .refunds(refundsStats)
                .system(system)
                .build();
    }
}

