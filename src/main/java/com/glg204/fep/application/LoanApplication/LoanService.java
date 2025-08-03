package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.domain.LoanDomain.*;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public Loan createLoan(LoanRequestDTO dto, User lender) {
        User borrower = userRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Loan loan = Loan.builder()
                .amount(dto.getAmount())
                .interestRate(dto.getInterestRate())
                .durationInMonths(dto.getDurationInMonths())
                .status(LoanStatus.PENDING)
                .startDate(LocalDate.now())
                .lender(lender)
                .borrower(borrower)
                .build();

        return loanRepository.save(loan);
    }
}
