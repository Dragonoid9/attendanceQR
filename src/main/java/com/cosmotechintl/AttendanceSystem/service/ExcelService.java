package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDto;
import org.springframework.http.ResponseEntity;

public interface ExcelService {

    ResponseEntity<?>  attendanceExportToExcel(AttendanceRequestDto attendanceRequestDto);
}
