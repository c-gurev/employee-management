package com.employee.management.servlet;

import com.employee.management.dto.CheckEmailRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/check-email")
public class CheckEmailServlet extends EmployeeManagementServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader();
             PrintWriter writer = response.getWriter()) {
            CheckEmailRequest checkEmailRequest = mapper.readValue(reader, CheckEmailRequest.class);
            String email = checkEmailRequest.getEmail();
            Integer employeeId = checkEmailRequest.getEmployeeId();

            boolean exists = employeeDAO.checkEmailExists(email, employeeId);
            mapper.writeValue(writer, Map.of("exists", exists));
        } catch (Exception e) {
            logger.error("Error deleting employee records", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponseData(e.getMessage(), response.getWriter());
        }
    }
}
