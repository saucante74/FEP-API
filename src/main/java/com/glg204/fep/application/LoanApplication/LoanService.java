package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanResponseDTO createLoan(LoanRequestDTO dto, User lender) {
        User borrower = userRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new NoSuchElementException("Borrower not found"));

        Loan loan = new Loan();
        loan.setAmount(dto.getAmount());
        loan.setInterestRate(dto.getInterestRate());
        loan.setDurationInMonths(dto.getDurationInMonths());
        loan.setStatus(LoanStatus.PENDING);
        loan.setLender(lender);
        loan.setBorrower(borrower);

        loanRepository.save(loan);
        return toDto(loan);
    }

    public List<LoanResponseDTO> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LoanResponseDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));
        return toDto(loan);
    }

    public LoanResponseDTO updateLoan(Long id, LoanRequestDTO dto) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));

        User borrower = userRepository.findById(dto.getBorrowerId())
                .orElseThrow(() -> new NoSuchElementException("Borrower not found"));

        loan.setAmount(dto.getAmount());
        loan.setInterestRate(dto.getInterestRate());
        loan.setDurationInMonths(dto.getDurationInMonths());
        loan.setStatus(LoanStatus.valueOf(dto.getStatus()));
        loan.setBorrower(borrower);

        loanRepository.save(loan);
        return toDto(loan);
    }

    public void deleteLoan(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new NoSuchElementException("Loan not found");
        }
        loanRepository.deleteById(id);
    }

    private LoanResponseDTO toDto(Loan loan) {
        return LoanResponseDTO.builder()
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .durationInMonths(loan.getDurationInMonths())
                .status(String.valueOf(loan.getStatus()))
                .borrowerId(
                        loan.getBorrower() != null
                                ? loan.getBorrower().getId()
                                : null
                )
                .build();
    }
}
