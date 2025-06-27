package com.employee.management.servlet;

import com.employee.management.dao.EmployeeDAO;
import com.employee.management.dto.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddEmployeeServletSimpleTest {

    @InjectMocks
    AddEmployeeServlet servlet;

    @Spy
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Mock
    EmployeeDAO employeeDAO;

    @Mock
    Validator validator;

    HttpServletRequest request;
    HttpServletResponse response;
    StringWriter sw;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));
    }

    @Test
    void doPost_success() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        String json = "{\"name\":\"Test\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        when(request.getReader()).thenReturn(reader);
        doReturn(dto).when(mapper).readValue(eq(reader), eq(EmployeeDTO.class));
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(employeeDAO.addEmployee(dto)).thenReturn(true);

        servlet.doPost(request, response);
        String out = sw.toString();

        assertTrue(out.contains("\"status\":\"success\""));
        assertTrue(out.contains("Employee was successfully added"));
    }
}
