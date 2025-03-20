package org.yc.assignments.progresssoft.utils.mappers;

import org.mapstruct.Mapper;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.models.Deal;

@Mapper(componentModel = "spring")
public interface DealMapper {
    DealResponseDTO dealToResponseDTO(Deal deal);
    Deal requestDTOToDeal(DealRequestDTO dealRequestDTO);

}
