package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.RefundDomain.RefundStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {
    private Long loanId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "1000000", message = "Amount must be less than or equal to 1,000,000")
    private double amount;

    private RefundStatus status;
}


