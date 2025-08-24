package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.application.UserApplication.UserResponseDTO;
import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanDomainService;
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
        loan.setReference(LoanDomainService.generateReference());
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
        loan.setStatus(LoanStatus.valueOf(String.valueOf(dto.getStatus())));
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
        if (loan == null) {
            return null;
        }

        return LoanResponseDTO.builder()
                .id(loan.getId())
                .reference(loan.getReference())
                .amount(loan.getAmount())
                .interestRate(loan.getInterestRate())
                .durationInMonths(loan.getDurationInMonths())
                .status(loan.getStatus() != null ? loan.getStatus().name() : null)
                .borrower(
                        loan.getBorrower() != null
                                ? toUserDto(loan.getBorrower())
                                : null
                )
                .lender(
                        loan.getLender() != null
                                ? toUserDto(loan.getLender())
                                : null
                )
                .build();
    }

    private UserResponseDTO toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

}
