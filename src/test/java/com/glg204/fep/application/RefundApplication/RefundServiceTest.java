package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.RefundDomain.RefundStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefundServiceTest {

    private RefundRepository refundRepository;
    private LoanRepository loanRepository;
    private RefundNotificationService refundNotificationService;
    private RefundService refundService;

    @BeforeEach
    void setup() {
        refundRepository = mock(RefundRepository.class);
        loanRepository = mock(LoanRepository.class);
        refundNotificationService = mock(RefundNotificationService.class);
        refundService = new RefundService(refundRepository, loanRepository, refundNotificationService);
    }

    @Test
    void shouldCreateRefundSuccessfully() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReference("LOAN123");

        RefundRequestDTO dto = new RefundRequestDTO(1L, 500.0, RefundStatus.PENDING);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(refundRepository.save(any(Refund.class))).thenAnswer(invocation -> {
            Refund refund = invocation.getArgument(0);
            refund.setId(10L);
            refund.setLoan(loan);
            refund.setRefundDate(LocalDateTime.now());
            return refund;
        });

        RefundResponseDTO response = refundService.createRefund(dto);

        assertEquals(500.0, response.getAmount());
        assertEquals("LOAN123", response.getLoanReference());
        verify(refundNotificationService).sendRefundNotifications(any(Refund.class));
    }

    @Test
    void shouldThrowWhenLoanNotFoundOnCreate() {
        RefundRequestDTO dto = new RefundRequestDTO(99L, 100.0, RefundStatus.PENDING);
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> refundService.createRefund(dto));
    }

    @Test
    void shouldReturnRefundsForCurrentUser() {
        User user = new User();
        Refund refund = Refund.builder().amount(100.0).loan(new Loan()).build();

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = mock(SecurityContext.class);
            Authentication auth = mock(Authentication.class);
            when(SecurityContextHolder.getContext()).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(user);

            when(refundRepository.findByUserInvolved(user)).thenReturn(List.of(refund));

            List<RefundResponseDTO> result = refundService.getRefundsForCurrentUser();

            assertEquals(1, result.size());
            assertEquals(100.0, result.get(0).getAmount());
        }
    }

    @Test
    void shouldUpdateRefundSuccessfully() {
        Refund refund = Refund.builder().id(1L).amount(100.0).loan(new Loan()).build();
        Loan newLoan = new Loan();
        newLoan.setId(2L);
        newLoan.setReference("NEWREF");

        RefundRequestDTO dto = new RefundRequestDTO(2L, 200.0, RefundStatus.PENDING);

        when(refundRepository.findById(1L)).thenReturn(Optional.of(refund));
        when(loanRepository.findById(2L)).thenReturn(Optional.of(newLoan));
        when(refundRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefundResponseDTO updated = refundService.updateRefund(1L, dto);

        assertEquals(200.0, updated.getAmount());
        assertEquals("NEWREF", updated.getLoanReference());
    }

    @Test
    void shouldDeleteRefund() {
        refundService.deleteRefund(1L);
        verify(refundRepository).deleteById(1L);
    }
}
