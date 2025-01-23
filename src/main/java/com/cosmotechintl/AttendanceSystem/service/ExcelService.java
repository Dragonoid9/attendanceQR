package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    ResponseEntity<?> attendanceExportToExcel(AttendanceExportRequestDto attendanceExportRequestDto);

    ResponseEntity<?> exportEmptyUserExcelTemplate ();

    ApiResponse<?> importUsersFromExcel(MultipartFile file);
}
