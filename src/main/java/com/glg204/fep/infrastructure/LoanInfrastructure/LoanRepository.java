package com.glg204.fep.infrastructure.LoanInfrastructure;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
import com.glg204.fep.domain.UserDomain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByLender(User lender);
    List<Loan> findByBorrower(User borrower);
    List<Loan> findByStatus(LoanStatus status);
}