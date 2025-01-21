package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface ExcelService {

    ResponseEntity<?> attendanceExportToExcel(AttendanceExportRequestDto attendanceExportRequestDto);

    ResponseEntity<?> exportEmptyUserExcelTemplate (HttpServletResponse response);
}
