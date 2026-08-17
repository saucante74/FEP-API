package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.domain.RefundDomain.RefundStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RefundRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithValidData() {
        RefundRequestDTO dto = new RefundRequestDTO(1L, 100.0, RefundStatus.PENDING);
        Set<ConstraintViolation<RefundRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenAmountTooLow() {
        RefundRequestDTO dto = new RefundRequestDTO(1L, 0.0, RefundStatus.PENDING);
        Set<ConstraintViolation<RefundRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenAmountTooHigh() {
        RefundRequestDTO dto = new RefundRequestDTO(1L, 2_000_000.0, RefundStatus.PENDING);
        Set<ConstraintViolation<RefundRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenAmountIsNull() {
        RefundRequestDTO dto = new RefundRequestDTO(1L, 0, RefundStatus.PENDING);
        dto.setAmount(0);
        Set<ConstraintViolation<RefundRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
