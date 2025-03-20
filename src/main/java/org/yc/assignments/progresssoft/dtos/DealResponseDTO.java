package org.yc.assignments.progresssoft.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DealResponseDTO(String id,
                              String sourceCurrency,
                              String targetCurrency,
                              String amount,

                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                              LocalDateTime timestamp) {
}
