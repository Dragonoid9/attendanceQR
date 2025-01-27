package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/csv")
public class CSVController {

    @Autowired
    CSVService csvService;

    @PostMapping("/export/attendance")
    public ResponseEntity<?> AttendanceExportToCSV(@RequestBody AttendanceExportRequestDTO attendanceExportRequestDto) {
        return csvService.attendanceExportToCSV(attendanceExportRequestDto);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @GetMapping("/export/user-template")
    public ResponseEntity<?> exportUsersTemplateCSV() throws IOException {
        return csvService.exportEmptyUserCSVTemplate();
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/import/users")
    public ApiResponse<?> importUserFromCSV(@RequestParam("file") MultipartFile file) throws IOException {
        return csvService.importUsersFromCSV(file);
    }
}
