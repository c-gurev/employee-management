package com.employee.management.servlet;

import com.employee.management.dao.EmployeeDAO;
import com.employee.management.dto.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class EmployeeManagementServlet extends HttpServlet {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    protected ObjectMapper mapper;

    @Inject
    protected EmployeeDAO employeeDAO;

    @Inject
    protected Validator validator;

    protected void writeResponseData(String status, String message, PrintWriter writer) {
        try {
            mapper.writeValue(writer, Map.of("status", status, "message", message));
        } catch (IOException e) {
            logger.error("Error while writing response data", e);
        }
    }

    protected void writeErrorResponseData(String message, PrintWriter writer) {
        try {
            mapper.writeValue(writer, Map.of("status", "error", "message", message));
        } catch (IOException e) {
            logger.error("Error while writing response data", e);
        }
    }

    protected boolean isInputInvalid(EmployeeDTO employee, HttpServletResponse response) throws IOException {
        Set<ConstraintViolation<EmployeeDTO>> violations =
                validator.validate(employee);
        if (!violations.isEmpty()) {
            writeValidationErrors(response, violations);
            return true;
        }
        return false;
    }

    private void writeValidationErrors(HttpServletResponse response, Set<ConstraintViolation<EmployeeDTO>> violations) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        var errors = violations.stream().collect(
                Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )
        );
        mapper.writeValue(response.getWriter(), errors);
    }
}
