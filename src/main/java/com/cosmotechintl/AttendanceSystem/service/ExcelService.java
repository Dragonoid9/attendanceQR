package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {

    ResponseEntity<?> attendanceExportToExcel(AttendanceExportRequestDTO attendanceExportRequestDto);

    ResponseEntity<?> exportEmptyUserExcelTemplate ();

    ResponseEntity<?> exportEmptyUserExcelTemplateFromStorage();

    ApiResponse<?> importUsersFromExcel(MultipartFile file);
}
