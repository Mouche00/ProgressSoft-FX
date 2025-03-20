package org.yc.assignments.progresssoft.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.yc.assignments.progresssoft.utils.annotations.ValidCurrencyCodeFormat;

public class CurrencyCodeFormatValidator implements ConstraintValidator<ValidCurrencyCodeFormat, String> {

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext constraintValidatorContext) {
        if(currencyCode == null || currencyCode.isBlank()) {
            return false;
        }

        // The string to be evaluated must have exactly three uppercase characters
        return currencyCode.matches("[A-Z]{3}");
    }
}
