package com.glg204.fep.application.RefundApplication;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {
    private Long loanId;
    private double amount;
}

