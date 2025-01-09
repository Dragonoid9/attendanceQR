package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface AttendanceService {

    ApiResponse<?> generateQR();

    ApiResponse<?> checkIn(QRAttendance attendance);

    ApiResponse<?> checkOut(QRAttendance attendance);
}
