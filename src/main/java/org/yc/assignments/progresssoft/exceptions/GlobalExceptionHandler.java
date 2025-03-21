package org.yc.assignments.progresssoft.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yc.assignments.progresssoft.exceptions.custom.ArgumentValidationException;
import org.yc.assignments.progresssoft.exceptions.custom.DealListValidationException;
import org.yc.assignments.progresssoft.exceptions.custom.DealValidationException;
import org.yc.assignments.progresssoft.utils.responses.ApiResponse;

import java.util.Map;

import static org.yc.assignments.progresssoft.utils.helpers.ResponseHelper.errorResponse;
import static org.yc.assignments.progresssoft.utils.helpers.ResponseHelper.mixedResponse;
import static org.yc.assignments.progresssoft.utils.helpers.ValidationErrorsHelper.extractFieldErrors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DealValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleDealValidationException(DealValidationException e) {

        log.warn("Validation error: {}", e.getMessage());
        return mixedResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error(s)",
                e.getErrors()
        );
    }

    @ExceptionHandler(DealListValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDealListValidationException(DealListValidationException e) {

        log.warn("List validation error: {}", e.getMessage());
        return mixedResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error(s) in deal list, invalid deals have been ignored",
                Map.of("invalid_deals", e.getInvalidEntries(), "valid_deals", e.getValidEntries())
        );
    }
    @ExceptionHandler(ArgumentValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleArgumentValidationException(ArgumentValidationException e) {

        log.warn("Method argument validation error: {}", e.getMessage());
        return errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Method argument validation error",
                e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = extractFieldErrors(e.getBindingResult());

        log.warn("Validation failed: {}", validationErrors);
        return errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                validationErrors
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return errorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                e.getMessage()
        );
    }
}
