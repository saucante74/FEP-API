package com.glg204.fep.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class TwoDecimalValidator implements ConstraintValidator<TwoDecimal, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        BigDecimal bd = BigDecimal.valueOf(value);
        return bd.scale() <= 2;
    }
}
