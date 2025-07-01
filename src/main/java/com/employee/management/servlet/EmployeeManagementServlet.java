package com.employee.management.servlet;

import com.employee.management.dao.EmployeeDAO;
import com.employee.management.dto.EmployeeDTO;
import com.employee.management.dto.StatusResponse;
import com.employee.management.validation.EmployeeRecordValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public abstract class EmployeeManagementServlet extends HttpServlet {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    protected ObjectMapper mapper;

    @Inject
    protected EmployeeDAO employeeDAO;

    @Inject
    private EmployeeRecordValidator validationService;

    protected boolean isInputInvalid(EmployeeDTO employee, HttpServletResponse response) throws IOException {
        Map<String, String> validationErrors = validationService.getValidationErrors(employee);
        if (!validationErrors.isEmpty()) {
            writeErrorResponse(response, validationErrors);
            return true;
        }
        return false;
    }

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

    private void writeErrorResponse(HttpServletResponse response, Map<String, String> validationErrors) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        StatusResponse statusResponse = new StatusResponse("validation_error", "Input validation failed", validationErrors);
        mapper.writeValue(response.getWriter(), statusResponse);
    }
}
