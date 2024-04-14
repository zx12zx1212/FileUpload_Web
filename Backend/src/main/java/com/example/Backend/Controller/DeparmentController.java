package com.example.Backend.Controller;

import com.example.Backend.Entity.Dto.DepartmentDto;
import com.example.Backend.Service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class DeparmentController {
    @Autowired
    private DepartmentService departmentService;


    @GetMapping("/getAllDepartment")
    public ResponseEntity<List<DepartmentDto>> getAllDepartment() {
        return ResponseEntity.ok().body(departmentService.getAllDepartment());
    }

}
