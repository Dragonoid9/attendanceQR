package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.entity.Attendance;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.repository.AttendanceRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AttendanceServiceImpl implements AttendanceService {


    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;
    @Autowired
    private UserInfoRepository userInfoRepository;


    @Override
    public ApiResponse<?> generateQR() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime now = LocalDateTime.now();

            // Check if current time is after 4:00 PM for check-out QR
            boolean isAfter4PM = now.getHour() >= 16;
            String qrCodeData;
            String token;
            LocalDateTime expiration;

            if (isAfter4PM) {
                // Generate Check-Out QR Code
                expiration = today.atTime(23, 59, 59); // Expiry at midnight
                qrCodeData = qrCodeGenerator.generateQRCodeData("checkOut", expiration);
            } else {
                // Generate Check-In QR Code
                expiration = today.atTime(23, 59, 59); // Expiry at midnight
                qrCodeData = qrCodeGenerator.generateQRCodeData("checkIn", expiration);
            }

            byte[] image =qrCodeGenerator.getQRCodeImage(qrCodeData);

            // Step 5: Return the QR code
            return ResponseUtil.getSuccessResponse(image,"QR Code generated successfully.");

        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.CONFLICT);
        }
    }

    @Override
    public ApiResponse<?> checkIn(String accessToken,String expiration) {
        try {
            // Step 1: Validate token and expiration
            UserInfo userInfo = validateAccessTokenAndExpiration(accessToken,expiration);
            // Step 2: Save check-in data
            LocalDate today = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByUserInfoAndDate(userInfo,today);
            if (attendance.isPresent() && attendance.get().getCheckin() != null) {
                return ResponseUtil.getSuccessResponse("User has already checked in.");
            }
            Attendance attendanceCheckIn = Attendance.builder()
                    .checkin(LocalDateTime.now())
                    .userInfo(userInfo)
                    .date(today)
                    .build();

            attendanceRepository.save(attendanceCheckIn);

            return ResponseUtil.getSuccessResponse("User checked in successfully.");

        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.CONFLICT);
        }
    }

    @Override
    public ApiResponse<?> checkOut(String accessToken, String expiration) {
        try {
            // Step 1: Validate token and expiration
            UserInfo userInfo = validateAccessTokenAndExpiration(accessToken, expiration);

            // Step 2: Check if the user has already checked in
            LocalDate today = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByUserInfoAndDate(userInfo, today);

            // If no attendance record is found or user hasn't checked in yet
            if (!attendance.isPresent() || attendance.get().getCheckin() == null) {
                return ResponseUtil.getFailureResponse("User has not checked in yet. Cannot check out.",HttpStatus.BAD_REQUEST);
            }

            // Step 3: Update the attendance record with checkout time
            Attendance attendanceCheckOut = attendance.get();
            if (attendanceCheckOut.getCheckout() != null) {
                return ResponseUtil.getSuccessResponse("User has already checked out today.");
            }

            // Set the checkout time
            attendanceCheckOut.setCheckout(LocalDateTime.now());

            // Save the updated attendance record
            attendanceRepository.save(attendanceCheckOut);

            return ResponseUtil.getSuccessResponse("User checked out successfully.");

        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.CONFLICT);
        }
    }

    private UserInfo validateAccessTokenAndExpiration(String accessToken,String expiration) {

        String username = jwtService.extractUsername(accessToken);
        UserInfo userinfo = userInfoRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LocalDate today = LocalDate.now();
        // Check if the QR code is expired
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = LocalDateTime.parse(expiration);
        if (now.isAfter(expirationDate)) {
            throw new RuntimeException("QR code expired");
        }

        return userinfo;
    }
}
