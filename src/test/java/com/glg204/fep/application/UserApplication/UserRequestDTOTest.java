package com.glg204.fep.application.UserApplication;

import com.glg204.fep.domain.UserDomain.UserRole;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Validation tests for application layer
public class UserRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("john");
        dto.setEmail("invalid-email");
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setRole(UserRole.USER);
        dto.setPassword("secret");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername("john");
        dto.setEmail("john@example.com");
        dto.setFirstname("John");
        dto.setLastname("Doe");
        dto.setRole(UserRole.USER);
        dto.setPassword("secret");

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}
