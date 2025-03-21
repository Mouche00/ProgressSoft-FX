package org.yc.assignments.progresssoft.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.exceptions.custom.DealValidationException;
import org.yc.assignments.progresssoft.exceptions.custom.DealListValidationException;
import org.yc.assignments.progresssoft.exceptions.custom.InvalidCurrencyCodeException;
import org.yc.assignments.progresssoft.models.Deal;
import org.yc.assignments.progresssoft.repositories.DealRepository;
import org.yc.assignments.progresssoft.utils.mappers.DealMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;

    @InjectMocks
    private DealServiceImpl dealService;

    private DealRequestDTO validDealRequest;
    private DealRequestDTO invalidDealRequest;
    private DealResponseDTO dealResponse;
    private Deal deal;

    @BeforeEach
    void setUp() {
        validDealRequest = new DealRequestDTO("1", "USD", "EUR", 100.0);
        invalidDealRequest = new DealRequestDTO("2", "INVALID", "INVALID", 200.0);
        dealResponse = new DealResponseDTO("1", "USD", "EUR", 100.0, LocalDateTime.now());
        deal = new Deal("1", "USD", "EUR", 100.0, LocalDateTime.now());
    }

    @Test
    void fetchAll_ShouldReturnListOfDeals() {
        // Arrange
        when(dealRepository.findAll()).thenReturn(List.of(deal));
        when(dealMapper.dealsToResponseDTOs(List.of(deal))).thenReturn(List.of(dealResponse));

        // Act
        List<DealResponseDTO> result = dealService.fetchAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dealResponse, result.get(0));
        verify(dealRepository, times(1)).findAll();
        verify(dealMapper, times(1)).dealsToResponseDTOs(List.of(deal));
    }

    @Test
    void create_WithValidDeal_ShouldReturnDealResponse() {
        // Arrange
        when(dealMapper.requestDTOToDeal(validDealRequest)).thenReturn(deal);
        when(dealRepository.save(deal)).thenReturn(deal);
        when(dealMapper.dealToResponseDTO(deal)).thenReturn(dealResponse);

        // Act
        DealResponseDTO result = dealService.create(validDealRequest);

        // Assert
        assertNotNull(result);
        assertEquals(dealResponse, result);
        verify(dealRepository, times(1)).save(deal);
    }

    @Test
    void create_WithInvalidDeal_ShouldThrowDealValidationException() {
        // Arrange
        when(dealRepository.findById("2")).thenReturn(Optional.of(deal));

        // Act & Assert
        DealValidationException exception = assertThrows(DealValidationException.class, () -> dealService.create(invalidDealRequest));
        assertTrue(exception.getErrors().containsKey("id"));
        assertEquals("ID already exists", exception.getErrors().get("id"));
    }

    @Test
    void createBatch_WithValidDeals_ShouldReturnListOfDealResponses() {
        // Arrange
        List<DealRequestDTO> validDeals = List.of(validDealRequest);
        when(dealMapper.requestDTOsToDeals(validDeals)).thenReturn(List.of(deal));
        when(dealRepository.saveAll(List.of(deal))).thenReturn(List.of(deal));
        when(dealMapper.dealsToResponseDTOs(List.of(deal))).thenReturn(List.of(dealResponse));

        // Act
        List<DealResponseDTO> result = dealService.createBatch(validDeals);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dealResponse, result.get(0));
        verify(dealRepository, times(1)).saveAll(List.of(deal));
    }

    @Test
    void createBatch_WithInvalidDeals_ShouldThrowDealListValidationException() {
        // Arrange
        List<DealRequestDTO> invalidDeals = List.of(invalidDealRequest);
        when(dealRepository.findById("2")).thenReturn(Optional.of(deal));

        // Act & Assert
        DealListValidationException exception = assertThrows(DealListValidationException.class, () -> dealService.createBatch(invalidDeals));
        assertTrue(exception.getInvalidEntries().containsKey("Deal #2"));
        assertEquals("ID already exists", exception.getInvalidEntries().get("Deal #2").get("id"));
    }

    @Test
    void createBatch_WithMixedValidAndInvalidDeals_ShouldReturnValidDealsAndThrowException() {
        // Arrange
        List<DealRequestDTO> mixedDeals = List.of(validDealRequest, invalidDealRequest);
        when(dealRepository.findById("1")).thenReturn(Optional.empty());
        when(dealRepository.findById("2")).thenReturn(Optional.of(deal));
        when(dealMapper.requestDTOsToDeals(List.of(validDealRequest))).thenReturn(List.of(deal));
        when(dealRepository.saveAll(List.of(deal))).thenReturn(List.of(deal));
        when(dealMapper.dealsToResponseDTOs(List.of(deal))).thenReturn(List.of(dealResponse));

        // Act & Assert
        DealListValidationException exception = assertThrows(DealListValidationException.class, () -> dealService.createBatch(mixedDeals));
        assertEquals(1, exception.getInvalidEntries().size());
        assertEquals(dealResponse, exception.getValidEntries().get(0));
        assertTrue(exception.getInvalidEntries().containsKey("Deal #2"));
        assertEquals("ID already exists", exception.getInvalidEntries().get("Deal #2").get("id"));
    }

    @Test
    void validateConstraints_WithInvalidCurrency_ShouldReturnError() {
        // Arrange
        DealRequestDTO invalidCurrencyDeal = new DealRequestDTO("3", "INVALID", "EUR", 100.0);

        // Act
        Map<String, String> errors = dealService.validateConstraints(invalidCurrencyDeal);

        // Assert
        assertTrue(errors.containsKey("source_currency"));
        assertEquals("Invalid ISO 4217 currency code", errors.get("source_currency"));
    }

    @Test
    void validateConstraints_WithDuplicateCurrencies_ShouldReturnError() {
        // Arrange
        DealRequestDTO duplicateCurrencyDeal = new DealRequestDTO("4", "USD", "USD", 100.0);

        // Act
        Map<String, String> errors = dealService.validateConstraints(duplicateCurrencyDeal);

        // Assert
        assertTrue(errors.containsKey("source_currency/target_currency"));
        assertEquals("Source and target currencies cannot be the same", errors.get("source_currency/target_currency"));
    }

    @Test
    void validateConstraints_WithDuplicateId_ShouldReturnError() {
        // Arrange
        DealRequestDTO duplicateIdDeal = new DealRequestDTO("1", "USD", "EUR", 100.0);
        when(dealRepository.findById("1")).thenReturn(Optional.of(deal));

        // Act
        Map<String, String> errors = dealService.validateConstraints(duplicateIdDeal);

        // Assert
        assertTrue(errors.containsKey("id"));
        assertEquals("ID already exists", errors.get("id"));
    }

    @Test
    void isValidCurrency_WithValidCurrency_ShouldReturnTrue() {
        // Act
        boolean result = dealService.isValidCurrency("USD");

        // Assert
        assertTrue(result);
    }

    @Test
    void isValidCurrency_WithInvalidCurrency_ShouldReturnFalse() {
        // Act
        boolean result = dealService.isValidCurrency("INVALID");

        // Assert
        assertFalse(result);
    }

    @Test
    void areCurrenciesDuplicates_WithDifferentCurrencies_ShouldReturnFalse() {
        // Act
        boolean result = dealService.areCurrenciesDuplicates("USD", "EUR");

        // Assert
        assertFalse(result);
    }

    @Test
    void areCurrenciesDuplicates_WithSameCurrencies_ShouldReturnTrue() {
        // Act
        boolean result = dealService.areCurrenciesDuplicates("USD", "USD");

        // Assert
        assertTrue(result);
    }
}