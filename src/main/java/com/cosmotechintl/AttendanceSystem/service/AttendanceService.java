package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface AttendanceService {

    ApiResponse<?> getAttendanceQR();

    ApiResponse<?> checkIn(QRAttendance attendance);

    ApiResponse<?> checkOut(QRAttendance attendance);

    ApiResponse<?> getAttendance(AttendanceRequestDto attendanceRequestDto);

    ApiResponse<?> getOwnAttendanceByMonth(int month, int year);
}
