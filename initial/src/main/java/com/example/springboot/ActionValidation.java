package com.example.springboot;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class ActionValidation implements ConstraintValidator<Action, String> {
    @Override
    public boolean isValid(String action, ConstraintValidatorContext context) {
        if (!Objects.equals(action, "Buy") && !Objects.equals(action, "Sell")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void initialize(Action constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
