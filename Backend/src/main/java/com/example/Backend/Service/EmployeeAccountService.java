package com.example.Backend.Service;

import com.example.Backend.Entity.Dto.EmployeeAccountDto;

import java.util.Map;

public interface EmployeeAccountService {
    Map<String, String> login(EmployeeAccountDto employeeAccountDto);
}
