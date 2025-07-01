package com.employee.management.servlet;

import com.employee.management.dto.EmployeePage;
import com.employee.management.dto.PaginationInfo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/list-employees")
public class ListEmployeeServlet extends EmployeeManagementServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader();
             PrintWriter responseWriter = response.getWriter()) {
            PaginationInfo paginationInfo = mapper.readValue(reader, PaginationInfo.class);
            EmployeePage employeePage = employeeDAO.getEmployees(paginationInfo);
            mapper.writeValue(responseWriter, employeePage);
        } catch (Exception e) {
            logger.error("Error retrieving employee records", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponseData(e.getMessage(), response.getWriter());
        }
    }
}
