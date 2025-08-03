package com.glg204.fep.application.LoanApplication;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanResponseDTO {
    private BigDecimal amount;
    private Double interestRate;
    private Integer durationInMonths;
    private String status;
    private Long borrowerId;
}