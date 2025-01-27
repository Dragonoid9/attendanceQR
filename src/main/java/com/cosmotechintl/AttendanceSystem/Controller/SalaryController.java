package com.cosmotechintl.AttendanceSystem.Controller;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.SalaryRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/monthly-salary")
    public ApiResponse<?> getMonthlyAcquiredSalary(@RequestBody SalaryRequestDTO salaryRequestDto) {
    return salaryService.getMonthlySalary(salaryRequestDto);
    }
}
