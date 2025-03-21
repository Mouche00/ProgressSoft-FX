package org.yc.assignments.progresssoft.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.yc.assignments.progresssoft.dtos.DealRequestDTO;
import org.yc.assignments.progresssoft.exceptions.custom.ArgumentValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.yc.assignments.progresssoft.utils.helpers.ValidationErrorsHelper.*;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DealListValidationAspect {
    private final Validator validator;

    @Around("@annotation(org.yc.assignments.progresssoft.utils.annotations.ValidateDealList)")
    public Object validateDealList(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Starting validation of deal list");
        List<DealRequestDTO> dealRequestDTOs = getDealRequestDTOS(joinPoint);

        List<DealRequestDTO> validRequestDTOs = new ArrayList<>();
        Map<String, Map<String, String>> invalidRequestDTOsWithErrors = new HashMap<>();

        // For each deal in the list, validate it against the constraints present in the DealRequestDTO, and accordingly
        // either add it to the validRequestDTOs list or the invalidRequestDTOsWithErrors map
        for (DealRequestDTO dealRequestDTO : dealRequestDTOs) {
            BindingResult bindingResult = new BeanPropertyBindingResult(dealRequestDTO, "dealRequestDTO");
            validator.validate(dealRequestDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                log.warn("Validation errors found for Deal #{}", dealRequestDTO.id());
                invalidRequestDTOsWithErrors.put(
                        "Deal #" + dealRequestDTO.id(),
                        extractFieldErrors(bindingResult)
                );
            } else {
                validRequestDTOs.add(dealRequestDTO);
            }
        }

        if (!invalidRequestDTOsWithErrors.isEmpty()) {
            log.warn("Some deals have validation errors, caching errors");
            setCachedValidationErrors(invalidRequestDTOsWithErrors);
        } else {
            log.info("All deals validated successfully");
        }

        try {
            // Proceed with the filtered list valid deals
            return joinPoint.proceed(new Object[]{validRequestDTOs});
        } finally {
            // Clear cached errors after processing
            clearCachedValidationErrors();
            log.info("Validation process completed, cached errors cleared");
        }
    }

    private List<DealRequestDTO> getDealRequestDTOS(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<DealRequestDTO> dealRequestDTOs;

        if (args.length > 0 && args[0] instanceof List<?>) {
            try {
                dealRequestDTOs = (List<DealRequestDTO>) args[0];
            } catch (ClassCastException e) {
                log.error("First argument must be a List of DealRequestDTO");
                throw new ArgumentValidationException("First argument must be a List of DealRequestDTO");
            }
        } else {
            log.error("First argument must be a non-empty List of DealRequestDTO");
            throw new ArgumentValidationException("First argument must be a non-empty List of DealRequestDTO");
        }
        return dealRequestDTOs;
    }
}
