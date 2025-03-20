package org.yc.assignments.progresssoft.utils.responses;

import lombok.Getter;

@Getter
public class SuccessResponse<T> extends ApiResponse {
    private final T data;
    public SuccessResponse(int status, String message, T data) {
        super(true, status, message);
        this.data = data;
    }
}
