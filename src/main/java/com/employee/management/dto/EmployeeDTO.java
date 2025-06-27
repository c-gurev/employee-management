package com.employee.management.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class EmployeeDTO {

    private Integer id;

    @NotBlank(message = "Name must not be blank")
    @Pattern(
            regexp = "[A-Za-z ]+",
            message = "Name can only contain letters and spaces"
    )
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Pattern(
            // one or more allowed chars, '@', one or more domain chars, '.', 2–6 letter TLD
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be in the form user@domain.com"
    )
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(
            regexp = "^\\+?\\d+$",
            message = "Phone number must contain digits and may start with +"
    )
    private String phone;

    @NotNull(message = "Date of Joining is required")
    private LocalDate dateOfJoining;

    public EmployeeDTO() {
    }

    public EmployeeDTO(int id, String name, String email, String phone, LocalDate dateOfJoining) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateOfJoining = dateOfJoining;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                '}';
    }
}
