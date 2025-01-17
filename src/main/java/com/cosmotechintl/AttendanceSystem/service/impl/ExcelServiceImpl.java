package com.cosmotechintl.AttendanceSystem.service.impl;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.mapper.AttendanceCustomRepository;
import com.cosmotechintl.AttendanceSystem.service.ExcelService;
import com.cosmotechintl.AttendanceSystem.utility.ExcelUtility;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private final AttendanceCustomRepository attendanceCustomRepository;

    ExcelServiceImpl(AttendanceCustomRepository attendanceCustomRepository) {
        this.attendanceCustomRepository = attendanceCustomRepository;
    }

    public ResponseEntity<?> attendanceExportToExcel(AttendanceRequestDto attendanceRequestDto){

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

        // Generate Excel file
        String[] headers = {"Username","Check In","Check Out", "Date","Work Type"};
        ByteArrayInputStream excelStream = ExcelUtility.dataToExcel(records, "Attendance Report", headers);

        // Format current date for file name
        String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "attendance_report_" + formattedDate + ".xlsx";

        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=" + fileName);
        headersResponse.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(new InputStreamResource(excelStream));
    }
}
