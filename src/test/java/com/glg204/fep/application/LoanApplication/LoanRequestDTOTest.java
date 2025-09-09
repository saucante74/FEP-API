package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.domain.LoanDomain.LoanStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LoanRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithValidData() {
        LoanRequestDTO dto = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .interestRate(5.0)
                .durationInMonths(12)
                .status(LoanStatus.PENDING)
                .borrowerId(1L)
                .build();

        Set<ConstraintViolation<LoanRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenAmountIsTooLow() {
        LoanRequestDTO dto = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(50))
                .interestRate(5.0)
                .durationInMonths(12)
                .status(LoanStatus.PENDING)
                .borrowerId(1L)
                .build();

        Set<ConstraintViolation<LoanRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenInterestRateIsTooHigh() {
        LoanRequestDTO dto = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .interestRate(150.0)
                .durationInMonths(12)
                .status(LoanStatus.PENDING)
                .borrowerId(1L)
                .build();

        Set<ConstraintViolation<LoanRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenDurationIsTooLong() {
        LoanRequestDTO dto = LoanRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000))
                .interestRate(5.0)
                .durationInMonths(200)
                .status(LoanStatus.PENDING)
                .borrowerId(1L)
                .build();

        Set<ConstraintViolation<LoanRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenFieldsAreMissing() {
        LoanRequestDTO dto = LoanRequestDTO.builder().build();

        Set<ConstraintViolation<LoanRequestDTO>> violations = validator.validate(dto);
        assertEquals(5, violations.size());
    }
}
