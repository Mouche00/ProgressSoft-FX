package org.yc.assignments.progresssoft.utils.helpers;

import org.springframework.http.ResponseEntity;
import org.yc.assignments.progresssoft.utils.responses.ErrorResponse;
import org.yc.assignments.progresssoft.utils.responses.SuccessResponse;

import java.util.List;

public class ResponseHelper {

    public static <T> ResponseEntity<SuccessResponse<T>> successResponse(int status, String message, T data) {
        return ResponseEntity.status(status).body(
                new SuccessResponse<>(status, message, data)
        );
    }

    public static ResponseEntity<ErrorResponse> errorResponse(int status, String message, List<String> errors) {
        return ResponseEntity.status(status).body(
                new ErrorResponse(status, message, errors)
        );
    }
}
