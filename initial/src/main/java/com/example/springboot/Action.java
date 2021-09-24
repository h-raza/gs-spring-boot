package com.example.springboot;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ActionValidation.class)

public @interface Action {
    String message() default "Please enter either Buy or Sell";
    Class<?>[] groups() default{};
    public abstract Class<? extends Payload>[] payload() default {};

    //String[] values();
}
