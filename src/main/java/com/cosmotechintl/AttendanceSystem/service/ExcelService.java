package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import org.springframework.http.ResponseEntity;

public interface ExcelService {

    ResponseEntity<?> attendanceExportToExcel(AttendanceExportRequestDto attendanceExportRequestDto);
}
