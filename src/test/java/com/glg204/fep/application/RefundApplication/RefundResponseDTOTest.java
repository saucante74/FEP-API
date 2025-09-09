package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.RefundDomain.RefundStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RefundResponseDTOTest {

    @Test
    void shouldBuildRefundResponseDTOWhithoutErrors() {
        LocalDateTime now = LocalDateTime.now();

        RefundResponseDTO dto = RefundResponseDTO.builder()
                .id(1L)
                .loanReference("LOAN123")
                .refundDate(now)
                .amount(250.0)
                .status(RefundStatus.APPROVED)
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("LOAN123", dto.getLoanReference());
        assertEquals(250.0, dto.getAmount());
        assertEquals(RefundStatus.APPROVED, dto.getStatus());
        assertEquals(now, dto.getRefundDate());
    }
}
