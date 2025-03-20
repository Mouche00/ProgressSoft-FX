package org.yc.assignments.progresssoft.services;

import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;

import java.util.List;

public interface DealService {
    DealResponseDTO create(DealRequestDTO dealRequestDTO);
    List<DealResponseDTO> createBatch(List<DealRequestDTO> dealRequestDTOs);
    List<DealResponseDTO> fetchAll();

}
