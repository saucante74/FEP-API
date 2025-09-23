package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.RefundDomain.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundPatchDTO {
    private Long loanId;
    private Double amount;
    private RefundStatus status;
}
