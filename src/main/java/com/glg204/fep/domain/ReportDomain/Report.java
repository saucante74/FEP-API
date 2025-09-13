package com.glg204.fep.domain.ReportDomain;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.UserDomain.User;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_loan_id")
    private Loan reportedLoan;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    private boolean isOpen;

    private LocalDateTime reportDate;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;
}
