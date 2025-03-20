package org.yc.assignments.progresssoft.dtos;

import java.time.LocalDateTime;

public record DealResponseDTO(String id,
                              String sourceCurrency,
                              String targetCurrency,
                              String amount,
                              LocalDateTime timestamp) {
}
