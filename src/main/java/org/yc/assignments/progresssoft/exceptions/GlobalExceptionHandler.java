package org.yc.assignments.progresssoft.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yc.assignments.progresssoft.exceptions.custom.ArgumentValidationException;
import org.yc.assignments.progresssoft.utils.responses.ApiResponse;

import java.util.Map;

import static org.yc.assignments.progresssoft.utils.helpers.ResponseHelper.errorResponse;
import static org.yc.assignments.progresssoft.utils.helpers.ValidationErrorsHelper.extractFieldErrors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ArgumentValidationException.class)
    public ResponseEntity<ApiResponse<String>> handleListValidationException(ArgumentValidationException e) {

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

        log.warn("Validation Error(s): {}", validationErrors);
        return errorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error(s)",
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
