package com.bankcards.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueUserNameValidator.class)
public @interface UniqueUserNameValidation {

    public String message() default "User with name '%s' already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
