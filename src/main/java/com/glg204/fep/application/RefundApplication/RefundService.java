package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.RefundDomain.RefundStatus;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final RefundNotificationService refundNotificationService;

    public RefundResponseDTO createRefund(RefundRequestDTO dto) {
        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        Refund refund = Refund.builder()
                .amount(dto.getAmount())
                .refundDate(LocalDateTime.now())
                .status(RefundStatus.APPROVED)
                .loan(loan)
                .build();

        RefundResponseDTO responseDTO = toDto(refundRepository.save(refund));
        refundNotificationService.sendRefundNotifications(refund);

        return responseDTO;
    }

    public List<RefundResponseDTO> getAllRefunds() {
        return refundRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RefundResponseDTO getRefundById(Long id) {
        return refundRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found"));
    }

    public List<RefundResponseDTO> getRefundsByLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NoSuchElementException("Loan not found"));

        return refundRepository.findByLoan(loan).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<RefundResponseDTO> getRefundsForCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Refund> refunds = refundRepository.findByUserInvolved(user);

        return refunds.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RefundResponseDTO updateRefund(Long id, RefundRequestDTO dto) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found"));

        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        refund.setAmount(dto.getAmount());
        refund.setLoan(loan);

        return toDto(refundRepository.save(refund));
    }

    @Transactional
    public void deleteRefund(Long id) {
        refundRepository.deleteById(id);
    }

    private RefundResponseDTO toDto(Refund refund) {
        return RefundResponseDTO.builder()
                .id(refund.getId())
                .amount(refund.getAmount())
                .refundDate(refund.getRefundDate())
                .status(refund.getStatus())
                .loanReference(refund.getLoan().getReference())
                .build();
    }
}
