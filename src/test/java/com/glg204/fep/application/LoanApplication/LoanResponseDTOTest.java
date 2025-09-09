package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.application.UserApplication.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class LoanResponseDTOTest {

    @Test
    void shouldBuildLoanResponseDTOWhithoutErrors() {
        UserResponseDTO lender = UserResponseDTO.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .build();

        UserResponseDTO borrower = UserResponseDTO.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Jones")
                .email("bob@example.com")
                .build();

        LoanResponseDTO dto = LoanResponseDTO.builder()
                .id(10L)
                .reference("REF123")
                .amount(BigDecimal.valueOf(1000))
                .interestRate(5.0)
                .durationInMonths(12)
                .status("PENDING")
                .lender(lender)
                .borrower(borrower)
                .build();

        assertEquals(10L, dto.getId());
        assertEquals("REF123", dto.getReference());
        assertEquals(BigDecimal.valueOf(1000), dto.getAmount());
        assertEquals(5.0, dto.getInterestRate());
        assertEquals(12, dto.getDurationInMonths());
        assertEquals("PENDING", dto.getStatus());
        assertEquals("Alice", dto.getLender().getFirstName());
        assertEquals("Bob", dto.getBorrower().getFirstName());
    }
}
