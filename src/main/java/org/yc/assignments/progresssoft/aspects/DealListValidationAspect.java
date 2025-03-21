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
        List<DealRequestDTO> dealRequestDTOs = getDealRequestDTOS(joinPoint);

        List<DealRequestDTO> validRequestDTOs = new ArrayList<>();
        Map<String, Map<String, String>> invalidRequestDTOsWithErrors = new HashMap<>();

        for (DealRequestDTO dealRequestDTO : dealRequestDTOs) {
            BindingResult bindingResult = new BeanPropertyBindingResult(dealRequestDTO, "dealRequestDTO");
            validator.validate(dealRequestDTO, bindingResult);

            if (bindingResult.hasErrors()) {
                invalidRequestDTOsWithErrors.put(
                        "Deal #" + dealRequestDTO.id(),
                        extractFieldErrors(bindingResult)
                );
            } else {
                validRequestDTOs.add(dealRequestDTO);
            }
        }

        if (!invalidRequestDTOsWithErrors.isEmpty()) {
            setCachedValidationErrors(invalidRequestDTOsWithErrors);
        }

        try {
            return joinPoint.proceed(new Object[]{validRequestDTOs});
        } finally {
            clearCachedValidationErrors();
        }
    }

    private List<DealRequestDTO> getDealRequestDTOS(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<DealRequestDTO> dealRequestDTOs;

        if (args.length > 0 && args[0] instanceof List<?>) {
            try {
                dealRequestDTOs = (List<DealRequestDTO>) args[0];
            } catch (ClassCastException e) {
                throw new ArgumentValidationException("First argument must be a List of DealRequestDTO");
            }
        } else {
            throw new ArgumentValidationException("First argument must be a non-empty List of DealRequestDTO");
        }
        return dealRequestDTOs;
    }
}
