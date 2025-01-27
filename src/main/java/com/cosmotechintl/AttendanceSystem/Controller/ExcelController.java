package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/export/attendance")
    public ResponseEntity<?> AttendanceExportToExcel(@RequestBody AttendanceExportRequestDTO attendanceExportRequestDto) {
        return excelService.attendanceExportToExcel(attendanceExportRequestDto);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @GetMapping("/export/user-template")
    public ResponseEntity<?> exportUsersTemplate() throws IOException {
        return excelService.exportEmptyUserExcelTemplate();
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @GetMapping("/export/user-template-storage")
    public ResponseEntity<?> exportUsersTemplateFromStorage() throws IOException {
        return excelService.exportEmptyUserExcelTemplateFromStorage();
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/import/users")
    public ApiResponse<?> importUserFromExcel(@RequestParam("file")MultipartFile file) throws IOException {
        return excelService.importUsersFromExcel(file);
    }
}
