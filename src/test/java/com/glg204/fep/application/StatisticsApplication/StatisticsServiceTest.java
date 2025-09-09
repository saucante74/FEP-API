package com.glg204.fep.application.StatisticsApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests
public class StatisticsServiceTest {

    private final LoanRepository loanRepository = mock(LoanRepository.class);
    private final RefundRepository refundRepository = mock(RefundRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ReportRepository reportRepository = mock(ReportRepository.class);

    private StatisticsService statisticsService;

    @BeforeEach
    void setup() {
        statisticsService = new StatisticsService(loanRepository, refundRepository, userRepository, reportRepository);
    }

    @Test
    void shouldCalculateGlobalStatsWithSuccess() {
        Loan loan = new Loan();
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setStatus(LoanStatus.IN_PROGRESS);
        loan.setInterestRate(5.0);
        loan.setDurationInMonths(24);

        when(loanRepository.findAll()).thenReturn(List.of(loan));
        when(userRepository.findAll()).thenReturn(List.of(new User()));
        when(userRepository.count()).thenReturn(1L);
        when(refundRepository.count()).thenReturn(5L);

        GlobalStatsDTO stats = statisticsService.getGlobalStats();

        assertEquals(1000, stats.getTotalFinanced());
        assertEquals(1, stats.getTotalCustomers());
        assertEquals(1, stats.getLoansInProgress());
        assertEquals(5, stats.getRefundsInProgress());
    }
}
