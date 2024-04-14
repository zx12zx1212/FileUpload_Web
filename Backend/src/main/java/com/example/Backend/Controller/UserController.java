package com.example.Backend.Controller;

import com.example.Backend.Entity.Dto.EmployeeAccountDto;
import com.example.Backend.Entity.Dto.EmployeeDetailDto;
import com.example.Backend.Entity.Employee;
import com.example.Backend.Service.EmployeeAccountService;
import com.example.Backend.Service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeAccountService employeeAccountService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/createEmployee")
    ResponseEntity<EmployeeDetailDto> createEmployee(@RequestBody EmployeeDetailDto employee) {
        Employee entity = employeeService.createEmployee(employee);
        EmployeeDetailDto dto = modelMapper.map(entity, EmployeeDetailDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(path = "/searchEmployee/{employeeId}", produces = "application/json;charset=UTF-8")
    ResponseEntity<?> searchEmployee(@PathVariable("employeeId") String employeeId) {
        Employee employee = employeeService.searchEmployee(employeeId);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Not Found " + employeeId);
        } else {
            return ResponseEntity.ok().body(modelMapper.map(employeeService.searchEmployee(employeeId), EmployeeDetailDto.class));
        }
    }

    @PostMapping("modifyEmployee/{employeeId}")
    ResponseEntity<EmployeeDetailDto> modifyEmployee(@RequestBody EmployeeDetailDto employee) {
        return ResponseEntity.ok().body(modelMapper.map(employeeService.modifyEmployee(employee), EmployeeDetailDto.class));
    }


    @DeleteMapping("/deleteEmployee/{employeeId}")
    ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") String employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    ResponseEntity<?> login(EmployeeAccountDto employeeAccountDto) {
        Map<String, String> empInfo = employeeAccountService.login(employeeAccountDto);
        if(empInfo == null){
            return ResponseEntity.accepted().body("帳號或密碼錯誤！");
        }
        return ResponseEntity.ok().body(empInfo);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok().body("logout");
    }

    @GetMapping("/welcome")
    public String greeting() {
        return "welcome";
    }

}
