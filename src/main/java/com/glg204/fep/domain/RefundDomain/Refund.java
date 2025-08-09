package com.glg204.fep.domain.RefundDomain;

import com.glg204.fep.domain.LoanDomain.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDateTime refundDate;

    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @ManyToOne
    private Loan loan;
}

