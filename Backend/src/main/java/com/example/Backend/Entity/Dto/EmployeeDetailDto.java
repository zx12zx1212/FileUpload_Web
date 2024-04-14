package com.example.Backend.Entity.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDetailDto {
    private String id;
    private String name;
    private String departmentId;
    private String email;
    private LocalDate birthday;
    private String address;
}
