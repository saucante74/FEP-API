package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.RefundDomain.RefundStatus;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final LoanRepository loanRepository;

    public RefundResponseDTO createRefund(RefundRequestDTO dto) {
        Loan loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        Refund refund = Refund.builder()
                .amount(dto.getAmount())
                .refundDate(LocalDateTime.now())
                .status(RefundStatus.APPROVED)
                .loan(loan)
                .build();

        Refund savedRefund = refundRepository.save(refund);
        return toDto(savedRefund);
    }

    private RefundResponseDTO toDto(Refund refund) {
        return RefundResponseDTO.builder()
                .id(refund.getId())
                .amount(refund.getAmount())
                .refundDate(refund.getRefundDate())
                .status(refund.getStatus())
                .loanId(refund.getLoan().getId())
                .build();
    }

}


