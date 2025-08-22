package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.application.UserApplication.UserResponseDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanResponseDTO {
    private Long id;
    private String reference;
    private BigDecimal amount;
    private Double interestRate;
    private Integer durationInMonths;
    private String status;
    private UserResponseDTO lender;
    private UserResponseDTO borrower;
}
