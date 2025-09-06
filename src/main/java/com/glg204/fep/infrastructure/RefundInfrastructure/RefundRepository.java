package com.glg204.fep.infrastructure.RefundInfrastructure;

import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.UserDomain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByLoan_Lender(User lender);
    List<Refund> findByLoan_Borrower(User borrower);
    @Query("SELECT r FROM Refund r WHERE r.loan.borrower = :user OR r.loan.lender = :user")
    List<Refund> findByUserInvolved(@Param("user") User user);
    List<Refund> findByLoan(Loan loan);
}

