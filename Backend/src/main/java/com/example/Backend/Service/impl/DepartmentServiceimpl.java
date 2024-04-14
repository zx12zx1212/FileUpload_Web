package com.example.Backend.Service.impl;

import com.example.Backend.Entity.Department;
import com.example.Backend.Entity.Dto.DepartmentDto;
import com.example.Backend.Entity.Dto.EmployeeListDto;
import com.example.Backend.Entity.Employee;
import com.example.Backend.Repository.DepartmentRepository;
import com.example.Backend.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceimpl implements DepartmentService {

//    @Autowired
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceimpl(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Cacheable("getAllDepartment")
    public List<DepartmentDto> getAllDepartment() {
        List<Department> listDep = departmentRepository.findAll();
        List<DepartmentDto> result = new ArrayList<>();
        for (Department dep : listDep) {
            DepartmentDto tmp_dep = DepartmentDto.builder()
                    .departmentId(dep.getId())
                    .departmentName(dep.getName()).build();
            List<EmployeeListDto> tmp_employee = new ArrayList<>();
            for (Employee e : dep.getEmployee()) {
                EmployeeListDto tmp_1 = EmployeeListDto.builder()
                        .id(e.getId())
                        .name(e.getName()).build();
                tmp_employee.add(tmp_1);
            }
            tmp_dep.setEmployeeListDto(tmp_employee);
            result.add(tmp_dep);
        }
        return result;
    }

}
