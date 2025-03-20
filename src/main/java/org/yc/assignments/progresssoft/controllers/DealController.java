package org.yc.assignments.progresssoft.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.services.DealService;

@Slf4j
@RestController
@RequestMapping("/api/v1/deals")
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;
    @PostMapping("/create")
    public ResponseEntity<DealResponseDTO> create(@RequestBody @Valid DealRequestDTO dealRequestDTO) {
        log.info("Received deal request: {}", dealRequestDTO);
        DealResponseDTO responseDTO = dealService.create(dealRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}

