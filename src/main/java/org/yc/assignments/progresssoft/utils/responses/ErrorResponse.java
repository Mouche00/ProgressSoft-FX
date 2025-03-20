package org.yc.assignments.progresssoft.utils.responses;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse extends ApiResponse{
    private final List<String > errors;
    public ErrorResponse(int status, String message, List<String> errors) {
        super(false, status, message);
        this.errors = errors;
    }
}
