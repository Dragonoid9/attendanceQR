package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AttendanceService {

    ResponseEntity<?> getCheckInQR();

    ResponseEntity<?> getCheckOutQR();

    ApiResponse<?> checkIn(QRAttendance attendance);

    ApiResponse<?> checkOut(QRAttendance attendance);
}
