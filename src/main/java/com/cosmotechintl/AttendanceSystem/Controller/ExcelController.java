package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import com.cosmotechintl.AttendanceSystem.service.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/export/attendance")
    public ResponseEntity<?> AttendanceExportToExcel(@RequestBody AttendanceExportRequestDto attendanceExportRequestDto) {
        return excelService.attendanceExportToExcel(attendanceExportRequestDto);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @GetMapping("/export/user-template")
    public ResponseEntity<?> exportUsersTemplate(HttpServletResponse response) throws IOException {
        return excelService.exportEmptyUserExcelTemplate(response);
    }
}
