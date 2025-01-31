package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.SalaryRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface SalaryService {

    ApiResponse<?> getMonthlySalary(SalaryRequestDTO salaryRequestDto);
}
