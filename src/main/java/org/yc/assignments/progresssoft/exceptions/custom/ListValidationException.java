package org.yc.assignments.progresssoft.exceptions.custom;

import lombok.Getter;

import java.util.Map;

@Getter
public class ListValidationException extends RuntimeException {
    Map<String, Map<String, String>> errors;
    public ListValidationException(String message, Map<String, Map<String, String>> errors) {
        super(message);
        this.errors = errors;
    }
}
