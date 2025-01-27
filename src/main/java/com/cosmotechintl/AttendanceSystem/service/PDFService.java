package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDTO;
import org.springframework.http.ResponseEntity;

public interface PDFService {

    ResponseEntity<?> attendanceExportToPDF(AttendanceExportRequestDTO attendanceRequestDto);
}
