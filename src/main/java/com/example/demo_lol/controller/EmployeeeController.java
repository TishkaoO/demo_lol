package com.example.demo_lol.controller;

import com.example.demo_lol.dto.TestEmpl;
import com.example.demo_lol.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeeController {
    private final EmployeeService employeeService;

    @PostMapping("/save")
    public void save(@RequestBody TestEmpl empl) {
        employeeService.saveOrUpdate(empl);
    }
}
