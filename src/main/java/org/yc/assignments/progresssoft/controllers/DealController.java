package org.yc.assignments.progresssoft.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.services.DealService;
import org.yc.assignments.progresssoft.utils.annotations.ValidateDealList;
import org.yc.assignments.progresssoft.utils.responses.ApiResponse;

import java.util.List;

import static org.yc.assignments.progresssoft.utils.helpers.ResponseHelper.successResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<DealResponseDTO>> create(@RequestBody @Valid DealRequestDTO dealRequestDTO) {
        log.info("Received deal creation request with the following data: {}", dealRequestDTO);
        DealResponseDTO dealResponseDTO = dealService.create(dealRequestDTO);
        return successResponse(
                HttpStatus.CREATED.value(),
                "Deal created successfully",
                dealResponseDTO
        );
    }

    // An aspect is used here to validate the list of deals without causing any rollback
    // it is according to the validation rules specified in the DealRequestDTO
    @ValidateDealList
    @PostMapping("/create/batch")
    public ResponseEntity<ApiResponse<List<DealResponseDTO>>> createBatch(@RequestBody List<DealRequestDTO> dealRequestDTOs) {
        log.info("Received batch deal creation request with the following data: {}", dealRequestDTOs);
        List<DealResponseDTO> dealResponseDTOs = dealService.createBatch(dealRequestDTOs);
        return successResponse(
                HttpStatus.CREATED.value(),
                "Deals created successfully",
                dealResponseDTOs
        );
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<ApiResponse<List<DealResponseDTO>>> fetchAll() {
        log.info("Received fetch all deals request");
        List<DealResponseDTO> dealResponseDTOs = dealService.fetchAll();
        return successResponse(
                HttpStatus.OK.value(),
                "Deals fetched successfully",
                dealResponseDTOs
        );
    }
}

