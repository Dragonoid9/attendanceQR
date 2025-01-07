package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface AttendanceService {

    ApiResponse<?> generateQR();

    ApiResponse<?> checkIn(String accessToken,String expiration);

    ApiResponse<?> checkOut(String accessToken,String expiration);
}
