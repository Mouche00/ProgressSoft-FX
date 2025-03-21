package org.yc.assignments.progresssoft.utils.helpers;

import org.springframework.http.ResponseEntity;
import org.yc.assignments.progresssoft.utils.responses.ApiResponse;

public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> successResponse(int status, String message, T data) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(true, status, message, data)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> errorResponse(int status, String message, T errors) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(false, status, message, errors)
        );
    }
}
