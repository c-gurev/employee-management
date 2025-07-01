package com.employee.management.servlet;

import com.employee.management.dto.DeleteEmployeesRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/delete-employee")
public class DeleteEmployeeServlet extends EmployeeManagementServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader();
             PrintWriter responseWriter = response.getWriter()) {
            DeleteEmployeesRequest deleteRequest = mapper.readValue(reader, DeleteEmployeesRequest.class);
            List<Integer> employeeIds = deleteRequest.getEmployeeIds();

            if (employeeIds == null || employeeIds.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No employee IDs provided");
                return;
            }
            int deletedCount = employeeDAO.deleteEmployee(employeeIds);
            if (deletedCount != 0) {
                writeResponseData("success", deletedCount + " record(s) successfully deleted", responseWriter);
            } else {
                writeResponseData("error", "No records were deleted", responseWriter);
            }
        } catch (Exception e) {
            logger.error("Error deleting employee records", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeErrorResponseData(e.getMessage(), response.getWriter());
        }
    }
}
