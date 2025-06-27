package com.employee.management.dto;

import java.util.List;

public class DeleteEmployeesRequest {

    private List<Integer> employeeIds;

    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }
}
