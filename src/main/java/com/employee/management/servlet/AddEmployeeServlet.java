package com.employee.management.servlet;

import com.employee.management.dto.EmployeeDTO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/add-employee")
public class AddEmployeeServlet extends EmployeeManagementServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader();
             PrintWriter responseWriter = response.getWriter()) {
            EmployeeDTO employee = mapper.readValue(reader, EmployeeDTO.class);
            if (isInputInvalid(employee, response)) {
                throw new IllegalArgumentException("Wrong request format");
            }
            if (employeeDAO.addEmployee(employee)) {
                writeResponseData("success", "Employee was successfully added", responseWriter);
            } else {
                writeResponseData("error", "Employee was not added", responseWriter);
            }
        } catch (Exception e) {
            logger.error("Error adding employee record", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponseData(e.getMessage().contains("ORA-20001: Email already exists")
                    ? "Email already exists" : e.getMessage(), response.getWriter());
        }
    }
}
