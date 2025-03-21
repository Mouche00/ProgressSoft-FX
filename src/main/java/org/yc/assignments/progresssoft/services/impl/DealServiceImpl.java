package org.yc.assignments.progresssoft.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.models.Deal;
import org.yc.assignments.progresssoft.repositories.DealRepository;
import org.yc.assignments.progresssoft.services.DealService;
import org.yc.assignments.progresssoft.utils.mappers.DealMapper;

import java.util.List;
import java.util.Map;

import static org.yc.assignments.progresssoft.utils.helpers.ValidationErrorsHelper.getCachedValidationErrors;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public DealResponseDTO create(DealRequestDTO dealRequestDTO) {
        Deal deal = dealRepository.save(
                dealMapper.requestDTOToDeal(dealRequestDTO)
        );

        return dealMapper.dealToResponseDTO(deal);
    }

    @Override
    public List<DealResponseDTO> createBatch(List<DealRequestDTO> dealRequestDTOs) {
//        Map<String, Map<String, String>> validationErrors = getCachedValidationErrors();

        List<Deal> deals = dealRepository.saveAll(
                dealMapper.requestDTOsToDeals(dealRequestDTOs)
        );

        return dealMapper.dealsToResponseDTOs(deals);
    }

    @Override
    public List<DealResponseDTO> fetchAll() {
        return dealMapper.dealsToResponseDTOs(
                dealRepository.findAll()
        );
    }
}
