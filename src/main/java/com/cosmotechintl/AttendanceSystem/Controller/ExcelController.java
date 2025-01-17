package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDto;
import com.cosmotechintl.AttendanceSystem.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/export")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/excel/attendance")
    public ResponseEntity<?> AttendanceExportToExcel(@RequestBody AttendanceRequestDto attendanceRequestDto) {
        return excelService.attendanceExportToExcel( attendanceRequestDto);
    }
}
