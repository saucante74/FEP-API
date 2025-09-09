package com.glg204.fep.infrastructure.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Validation unit tests
public class TwoDecimalValidatorTest {

    private TwoDecimalValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new TwoDecimalValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testNullValueIsValid() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    public void testValidTwoDecimalPlaces() {
        assertTrue(validator.isValid(123.45, context));
        assertTrue(validator.isValid(0.0, context));
        assertTrue(validator.isValid(999.99, context));
    }

    @Test
    public void testMoreThanTwoDecimalPlacesIsInvalid() {
        assertFalse(validator.isValid(123.456, context));
        assertFalse(validator.isValid(0.123, context));
    }
}
