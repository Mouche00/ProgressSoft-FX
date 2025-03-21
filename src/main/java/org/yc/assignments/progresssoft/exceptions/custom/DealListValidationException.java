package org.yc.assignments.progresssoft.exceptions.custom;

import lombok.Getter;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;

import java.util.List;
import java.util.Map;

@Getter
public class DealListValidationException extends RuntimeException {
    Map<String, Map<String, String>> invalidEntries;
    List<DealResponseDTO> validEntries;
    public DealListValidationException(Map<String, Map<String, String>> invalidEntries, List<DealResponseDTO> validEntries) {
        super("Validation Error(s) in list, some entries have been ignored");
        this.invalidEntries = invalidEntries;
        this.validEntries = validEntries;
    }
}
