package com.cosmotechintl.AttendanceSystem.service;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface AuthService {

    ApiResponse<?> loginUser(LoginRequestDTO loginRequestDTO);

    ApiResponse<?> generateRefreshToken(String refreshToken);

    ApiResponse<?> getAllRefreshTokens();

    ApiResponse<?> logoutUser(String accessToken);
 }
