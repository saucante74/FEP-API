package com.glg204.fep.infrastructure.RefundInfrastructure;

import com.glg204.fep.domain.RefundDomain.Refund;
import com.glg204.fep.domain.UserDomain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    List<Refund> findByLoan_Lender(User lender);

    List<Refund> findByLoan_Borrower(User borrower);
}

