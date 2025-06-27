package com.employee.management.dto;

import java.util.List;

public class EmployeePage {

    private List<EmployeeDTO> employees;

    private PaginationInfo paginationInfo;

    private int totalRecords;

    public EmployeePage() {
    }

    public EmployeePage(List<EmployeeDTO> employees, PaginationInfo paginationInfo, int totalRecords) {
        this.employees = employees;
        this.paginationInfo = paginationInfo;
        this.totalRecords = totalRecords;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public PaginationInfo getPaginationInfo() {
        return paginationInfo;
    }

    public void setPaginationInfo(PaginationInfo paginationInfo) {
        this.paginationInfo = paginationInfo;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
