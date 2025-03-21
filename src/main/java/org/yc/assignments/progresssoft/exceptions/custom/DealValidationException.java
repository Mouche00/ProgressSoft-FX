package org.yc.assignments.progresssoft.exceptions.custom;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DealValidationException extends RuntimeException {
    Map<String, String> errors = new HashMap<>();
    public DealValidationException(Map<String, String> errors) {
        super("Validation Error(s)");
        this.errors = errors;
    }
}
