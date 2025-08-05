package com.glg204.fep.infrastructure.RefundInfrastructure;

import com.glg204.fep.domain.RefundDomain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> { }

