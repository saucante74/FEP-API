package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private LoanNotificationService loanNotificationService;
    private LoanService loanService;

    @BeforeEach
    void setup() {
        loanRepository = mock(LoanRepository.class);
        userRepository = mock(UserRepository.class);
        loanNotificationService = mock(LoanNotificationService.class);
        loanService = new LoanService(loanRepository, userRepository, loanNotificationService);
    }

    @Test
    void shouldCreateLoanWithhSuccess() {
        User lender = new User();
        lender.setId(1L);
        lender.setEmail("lender@example.com");

        User borrower = new User();
        borrower.setId(2L);
        borrower.setEmail("borrower@example.com");
        borrower.setFirstName("John");

        LoanRequestDTO request = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .interestRate(5.0)
                .durationInMonths(12)
                .status(LoanStatus.PENDING)
                .borrowerId(2L)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(borrower));

        Loan savedLoan = new Loan();
        savedLoan.setId(10L);
        savedLoan.setAmount(request.getAmount());
        savedLoan.setInterestRate(request.getInterestRate());
        savedLoan.setDurationInMonths(request.getDurationInMonths());
        savedLoan.setStatus(LoanStatus.PENDING);
        savedLoan.setLender(lender);
        savedLoan.setBorrower(borrower);
        savedLoan.setReference("REF123");

        doAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(10L);
            loan.setReference("REF123");
            return null;
        }).when(loanRepository).save(any(Loan.class));

        LoanResponseDTO response = loanService.createLoan(request, lender);

        assertEquals("REF123", response.getReference());
        assertEquals(BigDecimal.valueOf(1000), response.getAmount());
        assertEquals(5.0, response.getInterestRate());
        verify(loanNotificationService).sendLoanRequestCreatedMail("borrower@example.com", "John", "REF123");
    }

    @Test
    void shouldThrowExceptionWhenBorrowerNotFound() {
        LoanRequestDTO request = LoanRequestDTO.builder()
                .borrowerId(99L)
                .build();

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> loanService.createLoan(request, new User()));
    }

    @Test
    void shouldReturnAllLoans() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setAmount(BigDecimal.valueOf(500));
        loan.setInterestRate(3.0);
        loan.setDurationInMonths(6);
        loan.setStatus(LoanStatus.IN_PROGRESS);

        when(loanRepository.findAll()).thenReturn(List.of(loan));

        List<LoanResponseDTO> loans = loanService.getAllLoans();

        assertEquals(1, loans.size());
        assertEquals(500, loans.get(0).getAmount().intValue());
    }

    @Test
    void shouldReturnLoansByUser() {
        User user = new User();
        user.setId(1L);

        Loan loan = new Loan();
        loan.setId(2L);
        loan.setLender(user);
        loan.setBorrower(user);
        loan.setAmount(BigDecimal.valueOf(700));

        try (MockedStatic<SecurityContextHolder> mocked = mockStatic(SecurityContextHolder.class)) {
            SecurityContext context = mock(SecurityContext.class);
            Authentication auth = mock(Authentication.class);
            when(SecurityContextHolder.getContext()).thenReturn(context);
            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(user);

            when(loanRepository.findByLenderOrBorrower(user, user)).thenReturn(List.of(loan));

            List<LoanResponseDTO> result = loanService.getLoansByUser();

            assertEquals(1, result.size());
            assertEquals(700, result.get(0).getAmount().intValue());
        }
    }

    @Test
    void shouldUpdateLoanWithSuccess() {
        Loan loan = new Loan();
        loan.setId(1L);

        User borrower = new User();
        borrower.setId(2L);

        LoanRequestDTO request = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(1500))
                .interestRate(4.5)
                .durationInMonths(24)
                .status(LoanStatus.IN_PROGRESS)
                .borrowerId(2L)
                .build();

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(userRepository.findById(2L)).thenReturn(Optional.of(borrower));

        LoanResponseDTO updated = loanService.updateLoan(1L, request);

        assertEquals(1500, updated.getAmount().intValue());
        assertEquals(4.5, updated.getInterestRate());
        assertEquals("IN_PROGRESS", updated.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistingLoan() {
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        LoanRequestDTO request = LoanRequestDTO.builder().borrowerId(1L).build();

        assertThrows(NoSuchElementException.class, () -> loanService.updateLoan(99L, request));
    }

    @Test
    void shouldDeleteLoanSuccessfully() {
        when(loanRepository.existsById(1L)).thenReturn(true);

        loanService.deleteLoan(1L);

        verify(loanRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentLoan() {
        when(loanRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> loanService.deleteLoan(99L));
    }
}
