package com.glg204.fep.application.StatisticsApplication;

import com.glg204.fep.application.DashboardApplication.user.UserDashboardStatsDTO;
import com.glg204.fep.domain.LoanDomain.*;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.RefundDomain.RefundDomainService;
import com.glg204.fep.domain.RefundDomain.RefundStatus;
import com.glg204.fep.domain.ReportDomain.Report;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public BigDecimal calculateInterestEarned(Loan loan) {
        return loanDomainService.calculateTotalInterest(loan);
    }

    public BigDecimal calculateInterestPaid(Loan loan) {
        return loanDomainService.calculateTotalInterest(loan);
    }

    public List<Loan> getLoansByUser(User user) {
        if (user.getRole() == UserRole.LENDER) {
            return loanRepository.findByLender(user);
        } else {
            return loanRepository.findByBorrower(user);
        }
    }

    public List<Refund> getRefundsByUser(User user) {
        if (user.getRole() == UserRole.LENDER) {
            return refundRepository.findByLoan_Lender(user);
        } else {
            return refundRepository.findByLoan_Borrower(user);
        }
    }

    public int calculateActiveLoansAmount(List<Loan> loans) {
        return loans.stream()
                .filter(l -> l.getStatus() != null && l.getStatus() == LoanStatus.IN_PROGRESS) // ou l.getStatus()==IN_PROGRESS
                .map(Loan::getAmount)
                .mapToInt(BigDecimal::intValue)
                .sum();
    }

    public int countLateRefunds(List<Refund> refunds) {
        return (int) refunds.stream()
                .filter(r -> r.getStatus() != null && r.getStatus() == RefundStatus.LATE)
                .count();
    }

    public List<UserDashboardStatsDTO.UpcomingRefundDTO> mapUpcomingRefunds(User user, List<Refund> refunds) {
        boolean isLender = user.getRole() == UserRole.LENDER;

        return refunds.stream()
                .filter(r -> r.getRefundDate() != null && r.getRefundDate().isAfter(java.time.LocalDateTime.now()))
                .sorted(Comparator.comparing(Refund::getRefundDate))
                .limit(5)
                .map(r -> UserDashboardStatsDTO.UpcomingRefundDTO.builder()
                        .dueDate(r.getRefundDate().toLocalDate().toString())
                        .amount((int) Math.round(r.getAmount()))
                        .status(r.getStatus() != null ? r.getStatus().name() : "UNKNOWN")
                        .counterparty(isLender
                                ? (r.getLoan()!=null && r.getLoan().getBorrower()!=null ? r.getLoan().getBorrower().getEmail() : "")
                                : (r.getLoan()!=null && r.getLoan().getLender()!=null ? r.getLoan().getLender().getEmail() : ""))
                        .build())
                .collect(Collectors.toList());
    }

}
