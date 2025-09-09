package com.glg204.fep.application.DashboardApplication.user;

import com.glg204.fep.application.StatisticsApplication.StatisticsService;
import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// UNit tests
public class UserDashboardServiceTest {

    private StatisticsService statisticsService;
    private UserDashboardService userDashboardService;

    @BeforeEach
    void setup() {
        statisticsService = mock(StatisticsService.class);
        userDashboardService = new UserDashboardService(statisticsService);
    }

    @Test
    void shouldReturnCorrectStatsForLender() {
        User lender = new User();
        lender.setRole(UserRole.LENDER);
        lender.setEmail("lender@example.com");

        Loan loan = new Loan();
        loan.setAmount(BigDecimal.valueOf(1000));
        loan.setLender(lender);
        loan.setStatus(LoanStatus.IN_PROGRESS);
        loan.setInterestRate(5.0);

        Refund refund = new Refund();

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = mock(SecurityContext.class);
            Authentication auth = mock(Authentication.class);
            when(SecurityContextHolder.getContext()).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(lender);

            when(statisticsService.getLoansByUser(lender)).thenReturn(List.of(loan));
            when(statisticsService.getRefundsByUser(lender)).thenReturn(List.of(refund));
            when(statisticsService.calculateInterestEarned(loan)).thenReturn(BigDecimal.valueOf(50));
            when(statisticsService.calculateActiveLoansAmount(any())).thenReturn(1000);
            when(statisticsService.countLateRefunds(any())).thenReturn(1);
            when(statisticsService.mapUpcomingRefunds(eq(lender), any())).thenReturn(List.of());

            UserDashboardStatsDTO stats = userDashboardService.getUserDashboardStats();

            assertEquals("Total prêté", stats.getAmountLabel());
            assertEquals("Intérêts gagnés", stats.getInterestsLabel());
            assertEquals(1000, stats.getTotalAmount());
            assertEquals(50, stats.getTotalInterests());
            assertEquals(1, stats.getLoansInProgress());
            assertEquals(1, stats.getLateRefundsCount());
        }
    }
}
