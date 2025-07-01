package com.employee.management.dto;

import java.util.Map;

public class StatusResponse {

    private String status;

    private String message;

    private Map<String, String> validationErrors;

    public StatusResponse() {
    }

    public StatusResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusResponse(String status, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
