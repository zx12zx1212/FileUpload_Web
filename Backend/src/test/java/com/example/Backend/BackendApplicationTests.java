package com.example.Backend;

import com.example.Backend.Entity.Employee;
import com.example.Backend.Service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeService employeeService;

    @Test
    void searchEmployee() {
        Employee employee = employeeService.searchEmployee("E00001");
        Assertions.assertEquals("王小明", employee.getName());
    }

//    @BeforeEach
//    public void setUp() throws Exception {
//        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
//                .setControllerAdvice(new GlobalConfiguration()).build();
//        EmployeeDetailDto dto;
//        dto = new EmployeeDetailDto();
//        dto.setId("E000010");
//        dto.setName("test");
//        dto.setEmail("test@gamil.com");
//        dto.setAddress("test");
//        dto.setBirthday(LocalDate.now());
//        dto.setDepartmentId("D002");
//    }

    @Test
    public void testUserControllerFail() throws Exception {
        ResultActions requestBuilder = mockMvc.perform(get("/searchEmployee/E000010123123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(equalTo("Not Found E000010123123")));
    }

    @Test
    public void testUserControllerSuccess() throws Exception {
        String result = "{\"id\":\"E00001\"," +
                "\"name\":\"王小明\"," +
                "\"departmentId\":\"D001\"," +
                "\"email\":\"E00001@gmail.com\"," +
                "\"birthday\":\"1990-01-01\"," +
                "\"address\":\"台北市中正區\"}";
        ResultActions requestBuilder = mockMvc.perform(get("/searchEmployee/E00001")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(equalTo(result)));
    }

}
