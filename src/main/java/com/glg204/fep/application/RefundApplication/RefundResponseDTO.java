package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.RefundDomain.RefundStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundResponseDTO {
    private Long id;
    private String loanReference;
    private LocalDateTime refundDate;
    private double amount;
    private RefundStatus status;
}

