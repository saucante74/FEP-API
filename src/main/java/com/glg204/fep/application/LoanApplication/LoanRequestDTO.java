package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.domain.LoanDomain.LoanStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanRequestDTO {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "100.00", message = "Amount must be at least 100")
    @DecimalMax(value = "1000000.00", message = "Amount must not exceed 1,000,000")
    private BigDecimal amount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.1", message = "Interest rate must be greater than 0")
    @DecimalMax(value = "100.0", message = "Interest rate must be less than or equal to 100")
    private Double interestRate;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 month")
    @Max(value = 120, message = "Duration must not exceed 120 months (10 years)")
    private Integer durationInMonths;

    @NotNull(message = "Status is required")
    private LoanStatus status;

    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;
}