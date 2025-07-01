package com.employee.management.dto;

public class PaginationInfo {

    private int pageNumber;

    private int pageSize;

    private String sortColumn;

    private String sortDirection;

    public PaginationInfo() {
    }

    public PaginationInfo(int pageNumber, int pageSize, String sortColumn, String sortDirection) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortColumn = sortColumn;
        this.sortDirection = sortDirection;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortColumn() {
        return switch (sortColumn) {
            case "name" -> "NAME";
            case "email" -> "EMAIL";
            case "phone" -> "PHONE_NUMBER";
            case "dateOfJoining" -> "DATE_OF_JOINING";
            default -> "EMPLOYEE_ID";
        };
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection() {
        if (sortDirection == null || sortDirection.isBlank()) {
            return "asc";
        }
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
