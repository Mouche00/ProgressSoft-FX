package org.yc.assignments.progresssoft.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.yc.assignments.progresssoft.utils.annotations.ValidCurrencyCodeFormat;

public record DealRequestDTO(
        @NotBlank(message = "Id is required")
        String id,

        @NotBlank(message = "Source currency code is required")
        @ValidCurrencyCodeFormat
        String sourceCurrency,

        @NotBlank(message = "Target currency code is required")
        @ValidCurrencyCodeFormat
        String targetCurrency,

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be a positive value")
        double amount) {
}
