package com.cosmotechintl.AttendanceSystem.service.impl;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.mapper.AttendanceCustomRepository;
import com.cosmotechintl.AttendanceSystem.service.PDFService;
import com.cosmotechintl.AttendanceSystem.utility.PDFUtility;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PDFServiceImpl implements PDFService {

    private final AttendanceCustomRepository attendanceCustomRepository;

    PDFServiceImpl(AttendanceCustomRepository attendanceCustomRepository) {
        this.attendanceCustomRepository = attendanceCustomRepository;
    }

    public ResponseEntity<?> attendanceExportToPDF(AttendanceExportRequestDto attendanceRequestDto){

        String username = attendanceRequestDto.getUsername();
        Integer month = attendanceRequestDto.getMonth();
        Integer year = attendanceRequestDto.getYear();
        String workType = attendanceRequestDto.getWorkType();
        String sortBy = attendanceRequestDto.getSortBy();
        String sortDirection = attendanceRequestDto.getSortDirection();

        // Set default values to null if they are 0 or not provided
        if (month != null && month == 0) month = null;
        if (year != null && year == 0) year = null;
        if (username != null && username.equals("string")) username = null;
        if (workType != null && workType.equals("string")) workType = null;
        if (sortBy != null && sortBy.equals("string")) sortBy = null;
        if (sortDirection != null && sortDirection.equals("string")) sortDirection = null;

        List<AttendanceResponseDto> records = attendanceCustomRepository.findAttendanceByCriteriaExcel(
                username, month, year, workType, sortBy, sortDirection);

        if (records.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No attendance records found");
        }
        String[] headers = {"S.N","Username","Check In","Check Out", "Date","Work Type"};
        String title = "Attendance Report";
        // Create DateTimeFormatter for 12-hour time format (e.g., "02:30 PM")
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Prepare data for the PDF file
        List<List<Object>> pdfData = new ArrayList<>();
        for (AttendanceResponseDto attendance : records) {
            List<Object> rowData = new ArrayList<>();
            rowData.add(attendance.getUsername());
            rowData.add(timeFormatter.format(attendance.getCheckIn()));
            rowData.add(attendance.getCheckOut() != null ? timeFormatter.format(attendance.getCheckOut()) : "");
            rowData.add(attendance.getDate().toString());
            rowData.add(attendance.getWorkType());
            pdfData.add(rowData);
        }


        ByteArrayInputStream pdfStream = PDFUtility.dataToPDF(title,headers,pdfData);

        // Format current date for file name
        String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "attendance_report_" + formattedDate + ".pdf";

        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=" + fileName);
        headersResponse.add("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(new InputStreamResource(pdfStream));
    }
}
