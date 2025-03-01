package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDTO;
import com.cosmotechintl.AttendanceSystem.service.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pdf")
public class PdfController {

    @Autowired
    private PDFService pdfService;

    @PostMapping("/export/attendance")
    public ResponseEntity<?> AttendanceExportToPDF(@RequestBody AttendanceExportRequestDTO attendanceExportRequestDto) {
        return pdfService.attendanceExportToPDF(attendanceExportRequestDto);
    }
}
