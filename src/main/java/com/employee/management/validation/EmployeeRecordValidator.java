package com.employee.management.validation;

import com.employee.management.dto.EmployeeDTO;

import java.util.Map;

public interface EmployeeRecordValidator {

    Map<String, String> getValidationErrors(EmployeeDTO employee);
}
