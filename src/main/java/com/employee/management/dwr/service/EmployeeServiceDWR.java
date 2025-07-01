package com.employee.management.dwr.service;

import com.employee.management.dao.EmployeeDAO;
import com.employee.management.dto.*;
import com.employee.management.validation.EmployeeRecordValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.Map;

@ApplicationScoped
@Named("employeeServiceDWR")
public class EmployeeServiceDWR {

    @Inject
    protected EmployeeDAO employeeDAO;

    @Inject
    private EmployeeRecordValidator validationService;

    public EmployeePage listEmployees(int pageNumber, int pageSize, String sortColumn, String sortDirection) throws SQLException {
        return employeeDAO.getEmployees(new PaginationInfo(pageNumber, pageSize, sortColumn, sortDirection));
    }

    public StatusResponse addEmployee(EmployeeDTO employee) throws SQLException {
        Map<String, String> validationErrors = validationService.getValidationErrors(employee);
        if (!validationErrors.isEmpty()) {
            return new StatusResponse("validation_error", "Input validation failed", validationErrors);
        }
        return employeeDAO.addEmployee(employee) ?
                new StatusResponse("success", "Employee record added") :
                new StatusResponse("error", "Error adding employee record");

    }

    public StatusResponse updateEmployee(EmployeeDTO employee) throws SQLException {
        Map<String, String> validationErrors = validationService.getValidationErrors(employee);
        if (!validationErrors.isEmpty()) {
            return new StatusResponse("validation_error", "Input validation failed", validationErrors);
        }
        return employeeDAO.updateEmployee(employee) ?
                new StatusResponse("success", "Employee record updated") :
                new StatusResponse("error", "Error updating employee record");
    }

    public int deleteEmployee(DeleteEmployeesRequest deleteEmployeesRequest) throws SQLException {
        return employeeDAO.deleteEmployee(deleteEmployeesRequest.getEmployeeIds());
    }

    public Map<String, Boolean> checkEmail(String email, Integer employeeId) throws SQLException {
        boolean exists = employeeDAO.checkEmailExists(email, employeeId);
        return Map.of("exists", exists);
    }
}
