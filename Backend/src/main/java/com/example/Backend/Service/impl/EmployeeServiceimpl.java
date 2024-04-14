package com.example.Backend.Service.impl;

import com.example.Backend.Entity.Dto.EmployeeDetailDto;
import com.example.Backend.Entity.Employee;
import com.example.Backend.Repository.EmployeeRepository;
import com.example.Backend.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeServiceimpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Employee createEmployee(EmployeeDetailDto employee) {
        Employee entity = new Employee();
        Employee.builder()
                .id(employee.getId())
                .name(employee.getName())
                .departmentId(employee.getDepartmentId())
                .email(employee.getEmail())
                .address(employee.getAddress())
                .birthday(employee.getBirthday()).build();
        return employeeRepository.save(entity);
    }

    @Override
    @Transactional
    public Employee modifyEmployee(EmployeeDetailDto employee) {
        Employee entity = searchEmployee(employee.getId());
        entity.setName(employee.getName());
        entity.setDepartmentId(employee.getDepartmentId());
        entity.setEmail(employee.getEmail());
        entity.setAddress(employee.getAddress());
        entity.setBirthday(employee.getBirthday());
        return employeeRepository.save(entity);
    }

    @Override
    public Employee searchEmployee(String employeeId) {
        Optional<Employee> result = employeeRepository.findById(employeeId);
        Employee employee = null;
        if(result.isPresent()){
            employee =  result.get();
        }
        return employee;
    }

    @Override
    @Transactional
    public void deleteEmployee(String employeeId) {
        Employee delete = searchEmployee(employeeId);
        employeeRepository.delete(delete);
    }

    @Override
    public String searchEmployeeDepId(String employeeId) {
        Optional<Employee> result = employeeRepository.findById(employeeId);
        return result.get().getDepartmentId();
    }

}
