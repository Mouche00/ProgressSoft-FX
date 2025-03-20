package org.yc.assignments.progresssoft.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.yc.assignments.progresssoft.utils.validators.CurrencyCodeFormatValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CurrencyCodeFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCurrencyCodeFormat {
    String message() default "Currency code must be in ISO 4217 format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
