package org.yc.assignments.progresssoft.utils.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class ApiResponse {
    private final boolean success;
    private final int status;
    private final String message;

    @JsonProperty("unix_timestamp")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private final Instant unixTimestamp;

    public ApiResponse(boolean success, int status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.unixTimestamp = Instant.now();
    }
}
