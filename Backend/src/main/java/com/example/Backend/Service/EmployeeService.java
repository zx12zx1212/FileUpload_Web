package com.example.Backend.Service;

import com.example.Backend.Entity.Dto.EmployeeDetailDto;
import com.example.Backend.Entity.Employee;
import org.springframework.stereotype.Service;


public interface EmployeeService {
    Employee createEmployee(EmployeeDetailDto employee);

    Employee modifyEmployee(EmployeeDetailDto employee);

    Employee searchEmployee(String employeeId);

    void deleteEmployee(String employeeId);

    String searchEmployeeDepId(String employeeId);

}
