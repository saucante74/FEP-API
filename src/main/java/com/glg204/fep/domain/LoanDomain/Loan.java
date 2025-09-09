package com.glg204.fep.domain.LoanDomain;

import com.glg204.fep.domain.UserDomain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;
    private BigDecimal amount;
    private Double interestRate;
    private Integer durationInMonths;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;
}
