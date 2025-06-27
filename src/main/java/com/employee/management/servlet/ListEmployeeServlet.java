package com.employee.management.servlet;

import com.employee.management.dto.EmployeePage;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/list-employees")
public class ListEmployeeServlet extends EmployeeManagementServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int pageNumber = parseIntOrDefault(request.getParameter("pageNumber"), 1);
        int pageSize = parseIntOrDefault(request.getParameter("pageSize"), 5);
        String sortColumn = getSortColumn(request);
        String sortDirection = request.getParameter("sortDirection");

        if (sortDirection == null || sortDirection.isBlank()) {
            sortDirection = "asc";
        }

        try (PrintWriter responseWriter = response.getWriter()) {
            EmployeePage employeePage = employeeDAO.getEmployees(
                    pageNumber, pageSize, sortColumn, sortDirection
            );
            mapper.writeValue(responseWriter, employeePage);
        } catch (Exception e) {
            logger.error("Error retrieving employee records", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponseData(e.getMessage(), response.getWriter());
        }
    }

    private String getSortColumn(HttpServletRequest request) {
        return switch (request.getParameter("sortColumn")) {
            case "id" -> "EMPLOYEE_ID";
            case "name" -> "NAME";
            case "email" -> "EMAIL";
            case "phone" -> "PHONE_NUMBER";
            case "dateOfJoining" -> "DATE_OF_JOINING";
            default -> "EMPLOYEE_ID";
        };
    }

    private int parseIntOrDefault(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
