package org.yc.assignments.progresssoft.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record DealResponseDTO(String id,

                              @JsonProperty("source_currency")
                              String sourceCurrency,

                              @JsonProperty("target_currency")
                              String targetCurrency,
                              double amount,

                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                              LocalDateTime timestamp) {
}
