package org.yc.assignments.progresssoft.utils.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ApiResponse<T>(boolean success,
                             int status,
                             String message,
                             T data,

                             @JsonProperty("unix_timestamp")
                             @JsonFormat(shape = JsonFormat.Shape.NUMBER)
                             Instant unixTimestamp) {

    public ApiResponse(boolean success, int status, String message, T data) {
        this(success, status, message, data, Instant.now());
    }
}
