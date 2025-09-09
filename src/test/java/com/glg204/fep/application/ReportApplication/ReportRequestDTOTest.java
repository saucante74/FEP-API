package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.ReportReason;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReportRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithValidData() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReportedUserId(1L);
        dto.setReason(ReportReason.SPAM);

        Set<ConstraintViolation<ReportRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenReasonIsNull() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReportedUserId(1L);
        dto.setReason(null);

        Set<ConstraintViolation<ReportRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("Reason is required", violations.iterator().next().getMessage());
    }
}
