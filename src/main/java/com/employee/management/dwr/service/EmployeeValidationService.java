package com.employee.management.dwr.service;

import com.employee.management.dto.EmployeeDTO;
import com.employee.management.validation.EmployeeRecordValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeValidationService implements EmployeeRecordValidator {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private Validator validator;

    @Override
    public Map<String, String> getValidationErrors(EmployeeDTO employee) {
        return getValidationErrors(validateInput(employee));
    }

    private Map<String, String> getValidationErrors(Set<ConstraintViolation<EmployeeDTO>> violations) {
        if (violations.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> errors = violations.stream().collect(
                Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )
        );
        logger.warn("Employee record input is invalid: {}", errors);
        return errors;
    }

    private Set<ConstraintViolation<EmployeeDTO>> validateInput(EmployeeDTO employee) {
        return validator.validate(employee);
    }
}
