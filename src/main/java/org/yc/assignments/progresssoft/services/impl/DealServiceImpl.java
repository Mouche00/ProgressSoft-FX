package org.yc.assignments.progresssoft.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.dtos.DealResponseDTO;
import org.yc.assignments.progresssoft.exceptions.custom.DealValidationException;
import org.yc.assignments.progresssoft.exceptions.custom.InvalidCurrencyCodeException;
import org.yc.assignments.progresssoft.exceptions.custom.DealListValidationException;
import org.yc.assignments.progresssoft.models.Deal;
import org.yc.assignments.progresssoft.repositories.DealRepository;
import org.yc.assignments.progresssoft.services.DealService;
import org.yc.assignments.progresssoft.utils.mappers.DealMapper;

import java.util.*;

import static org.yc.assignments.progresssoft.utils.helpers.ValidationErrorsHelper.getCachedValidationErrors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements DealService {
    private static final String INVALID_CURRENCY_MSG = "Invalid ISO 4217 currency code";
    private static final String DUPLICATE_CURRENCY_MSG = "Source and target currencies cannot be the same";
    private static final String DUPLICATE_ID_MSG = "ID already exists";

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public List<DealResponseDTO> fetchAll() {
        log.info("Fetching all deals");
        return dealMapper.dealsToResponseDTOs(
                dealRepository.findAll()
        );
    }

    @Override
    public DealResponseDTO create(DealRequestDTO dealRequestDTO) {
        log.info("Creating deal with the following data: {}", dealRequestDTO);
        Map<String, String> errors = validateConstraints(dealRequestDTO);

        if (!errors.isEmpty()) {
            throw new DealValidationException(errors);
        }

        Deal deal = dealRepository.save(
                dealMapper.requestDTOToDeal(dealRequestDTO)
        );

        log.info("Deal created successfully");

        return dealMapper.dealToResponseDTO(deal);
    }

    // In the case of batch creation, since it is specified that no rollback should be effected,
    // we should filter out the invalid deals and continue with persisting the valid ones
    // and in the response we should return all the deals, both valid and invalid, for a better user/dev experience
    @Override
    public List<DealResponseDTO> createBatch(List<DealRequestDTO> dealRequestDTOs) {
        log.info("Creating batch of deals with the following data: {}", dealRequestDTOs);
        Map<String, Map<String, String>> validationErrors = getCachedValidationErrors();

        dealRequestDTOs = dealRequestDTOs.stream().filter(d -> validateListConstraints(d, validationErrors)).toList();

        List<Deal> deals = dealRepository.saveAll(
                dealMapper.requestDTOsToDeals(dealRequestDTOs)
        );

        List<DealResponseDTO> dealResponseDTOs = dealMapper.dealsToResponseDTOs(deals);

        if (!validationErrors.isEmpty()) {
            throw new DealListValidationException(validationErrors, dealResponseDTOs);
        }

        log.info("Deals created successfully");

        return dealResponseDTOs;
    }

    private boolean validateListConstraints(DealRequestDTO dealRequestDTO, Map<String, Map<String, String>> validationErrors) {
        String dealKey = "Deal #" + dealRequestDTO.id();
        Map<String, String> errors = validateConstraints(dealRequestDTO);

        if (!errors.isEmpty()) {
            validationErrors.put(dealKey, errors);
            log.error("Validation errors in deal: {}", errors);
            return false;
        }
        return true;
    }

    // Since the constraints for currency and id are more complex, it was elected that
    // the constraints will be validated on the service-level, utilizing the repository layer for
    // some validation operations
    private Map<String, String> validateConstraints(DealRequestDTO dealRequestDTO) {
        Map<String, String> errors = new HashMap<>();
        validateSourceCurrency(dealRequestDTO, errors);
        validateTargetCurrency(dealRequestDTO, errors);
        checkCurrencyDuplicates(dealRequestDTO, errors);
        checkIdExistence(dealRequestDTO, errors);
        return errors;
    }

    private void validateSourceCurrency(DealRequestDTO dealRequestDTO, Map<String, String> errors) {
        if (!isValidCurrency(dealRequestDTO.sourceCurrency())) {
            errors.put("source_currency", INVALID_CURRENCY_MSG);
        }
    }

    private void validateTargetCurrency(DealRequestDTO dealRequestDTO, Map<String, String> errors) {
        if (!isValidCurrency(dealRequestDTO.targetCurrency())) {
            errors.put("target_currency", INVALID_CURRENCY_MSG);
        }
    }

    private void checkCurrencyDuplicates(DealRequestDTO dealRequestDTO, Map<String, String> errors) {
        if (areCurrenciesDuplicates(dealRequestDTO.sourceCurrency(), dealRequestDTO.targetCurrency())) {
            errors.put("source_currency/target_currency", DUPLICATE_CURRENCY_MSG);
        }
    }

    private void checkIdExistence(DealRequestDTO dealRequestDTO, Map<String, String> errors) {
        if (existsById(dealRequestDTO.id())) {
            errors.put("id", DUPLICATE_ID_MSG);
        }
    }

    private boolean areCurrenciesDuplicates(String sourceCurrency, String targetCurrency) {
        return sourceCurrency.equals(targetCurrency);
    }

    private boolean isValidCurrency(String sourceCurrency) {
        try {
            convertCurrencyToEntity(sourceCurrency);
            return true;
        } catch (InvalidCurrencyCodeException e) {
            return false;
        }
    }

    // Here we validate the currency using the Currency class, which throws an exception
    // when retrieving the instance of and invalid currency code
    private void convertCurrencyToEntity(String currency) {
        try{
            Currency.getInstance(currency);
        } catch (Exception e) {
            throw new InvalidCurrencyCodeException(currency);
        }
    }

    private Optional<Deal> findById(String id) {
        return dealRepository.findById(id);
    }

    private boolean existsById(String id) {
        return findById(id).isPresent();
    }
}
