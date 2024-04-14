package com.example.Backend.Service.impl;

import com.example.Backend.Entity.Dto.EmployeeAccountDto;
import com.example.Backend.Entity.Employee;
import com.example.Backend.Entity.EmployeeAccount;
import com.example.Backend.Repository.EmployeeAccountRepositry;
import com.example.Backend.Service.EmployeeAccountService;
import com.example.Backend.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeAccountServiceimpl implements EmployeeAccountService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeAccountRepositry employeeAccountRepositry;

    @Override
    public Map<String, String> login(EmployeeAccountDto employeeAccountDto) {
        EmployeeAccount user = employeeAccountRepositry.findByAccount(employeeAccountDto.getUsername());
        if (user == null) {
            return null;
        }
//        String hashedPasseword = DigestUtils.md5DigestAsHex(employeeAccountDto.getRawPassword().getBytes());
        String hashedPasseword = employeeAccountDto.getPassword();
        if (user.getPassword().equals(hashedPasseword)) {
            Employee emp = employeeService.searchEmployee(employeeAccountDto.getUsername());
            Map<String, String> response = Map.of("empId", emp.getId(), "empName", emp.getName());
            return response;
        } else {
            return null;
        }
    }
}
