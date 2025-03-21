package org.yc.assignments.progresssoft.exceptions.custom;

import lombok.Getter;

@Getter
public class InvalidCurrencyCodeException extends RuntimeException {
  private final String currency;
    public InvalidCurrencyCodeException(String currency) {
        super("Invalid ISO 4217 currency code: " + currency);
        this.currency = currency;
    }
}
