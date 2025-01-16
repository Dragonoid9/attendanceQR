package com.cosmotechintl.AttendanceSystem.service.impl;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.MyAttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.PagedResponseDto;
import com.cosmotechintl.AttendanceSystem.entity.Attendance;
import com.cosmotechintl.AttendanceSystem.entity.QR;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.mapper.AttendanceCustomRepository;
import com.cosmotechintl.AttendanceSystem.repository.AttendanceRepository;
import com.cosmotechintl.AttendanceSystem.repository.QRRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.service.AttendanceService;
import com.cosmotechintl.AttendanceSystem.service.JwtService;
import com.cosmotechintl.AttendanceSystem.service.QRCodeGenerator;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {


    private final AttendanceRepository attendanceRepository;

    private final JwtService jwtService;

    private final QRCodeGenerator qrCodeGenerator;

    private final UserInfoRepository userInfoRepository;
    private final QRRepository qrRepository;
    private final AttendanceCustomRepository attendanceCustomRepository;

    AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                          JwtService jwtService,
                          QRCodeGenerator qrCodeGenerator,
                          UserInfoRepository userInfoRepository,
                          QRRepository qrRepository,
                          AttendanceCustomRepository attendanceCustomRepository) {
        this.attendanceRepository = attendanceRepository;
        this.jwtService = jwtService;
        this.qrCodeGenerator = qrCodeGenerator;
        this.userInfoRepository = userInfoRepository;
        this.qrRepository = qrRepository;
        this.attendanceCustomRepository = attendanceCustomRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void generateQRDaily() {
        try {
            String token = UUID.randomUUID().toString();
            LocalDate date = LocalDate.now();
            String qrCodeDataCheckIn = qrCodeGenerator.generateQRCodeData("checkIn", token);
            String qrCodeDataCheckOut = qrCodeGenerator.generateQRCodeData("checkOut", token);

            // Storage directory for QR codes
            String storageDirectory = "qr_storage/";
            Files.createDirectories(Paths.get(storageDirectory));

            // Generate and save check-in QR code
            String checkInFilePath = storageDirectory + "checkin.png";
            qrCodeGenerator.saveQRCodeImage(qrCodeDataCheckIn, checkInFilePath);
            saveQRMetadata(token, date, checkInFilePath);

            // Generate and save check-out QR code
            String checkOutFilePath = storageDirectory + "checkout.png";
            qrCodeGenerator.saveQRCodeImage(qrCodeDataCheckOut, checkOutFilePath);
            saveQRMetadata(token, date, checkOutFilePath);
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error generating daily QR codes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "59 59 23 * * *")
    private void deleteDailyQR() {
        try {
            // Define the directory where QR codes are stored
            String storageDirectory = "qr_storage/";

            // List all files in the directory
            File directory = new File(storageDirectory);
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("QR code directory does not exist.");
                return;
            }

            // Delete all files in the directory
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".png")) {
                        if (file.delete()) {
                            System.out.println("Deleted file: " + file.getName());
                        } else {
                            System.out.println("Failed to delete file: " + file.getName());
                        }
                    }
                }
            }
            qrRepository.deleteAll();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while deleting daily QR codes: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> getAttendanceQR() {

        try {
            // Retrieve and validate the file paths for both QR codes
            String checkInPath = validateQRFilePath("qr_storage/checkin.png");
            String checkOutPath = validateQRFilePath("qr_storage/checkout.png");

            // Prepare the response data
            Map<String, String> paths = Map.of(
                    "checkInQR", checkInPath,
                    "checkOutQR", checkOutPath
            );
            return ResponseUtil.getSuccessResponse(paths, "QR Code generated successfully.");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.CONFLICT);
        }
    }

    // Helper method to validate and retrieve the file path
    private String validateQRFilePath(String filePath) throws ResourceNotFoundException {
        File qrFile = new File(filePath);

        // Check if the file exists
        if (!qrFile.exists()) {
            throw new ResourceNotFoundException("QR code file not found: " + filePath);
        }

        // Return the relative path of the file
        return filePath;
    }

    @Override
    public ApiResponse<?> checkIn(QRAttendance qrattendance) {
        String accessToken = qrattendance.getAccessToken();
        String token = qrattendance.getToken();
        String workType = qrattendance.getWorkType();

        try {
            // Step 1: Validate token and expiration
            UserInfo userInfo = validateAccessTokenAndUUIDToken(accessToken, token);
            // Step 2: Save check-in data
            LocalDate today = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByUserInfoAndDate(userInfo, today);
            if (attendance.isPresent() && attendance.get().getCheckin() != null) {
                return ResponseUtil.getSuccessResponse("User has already checked in.");
            }
            Attendance attendanceCheckIn = Attendance.builder()
                    .checkin(LocalDateTime.now())
                    .userInfo(userInfo)
                    .date(today)
                    .workType(workType)
                    .build();

            attendanceRepository.save(attendanceCheckIn);

            return ResponseUtil.getSuccessResponse("User checked in successfully.");

        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.CONFLICT);
        }
    }

    @Override
    public ApiResponse<?> checkOut(QRAttendance qrattendance) {
        String accessToken = qrattendance.getAccessToken();
        String token = qrattendance.getToken();
        try {
            // Step 1: Validate token and expiration
            UserInfo userInfo = validateAccessTokenAndUUIDToken(accessToken, token);

            // Step 2: Check if the user has already checked in
            LocalDate today = LocalDate.now();
            Optional<Attendance> attendance = attendanceRepository.findByUserInfoAndDate(userInfo, today);

            // If no attendance record is found or user hasn't checked in yet
            if (!attendance.isPresent() || attendance.get().getCheckin() == null) {
                return ResponseUtil.getFailureResponse("User has not checked in yet. Cannot check out.", HttpStatus.BAD_REQUEST);
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

    private UserInfo validateAccessTokenAndUUIDToken(String accessToken, String token) {

        String username = jwtService.extractUsername(accessToken);
        UserInfo userinfo = userInfoRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        LocalDate today = LocalDate.now();
        // Check if the QR code is expired
        boolean existQr = qrRepository.existsByTokenAndDate(token, today);
        if (!existQr) {
            throw new ResourceNotFoundException("QR token is not valid.");
        }
        return userinfo;
    }

    private void saveQRMetadata(String token, LocalDate date, String filePath) {
        QR qrData = new QR();
        qrData.setToken(token);
        qrData.setDate(date);
        qrData.setQr_url(filePath);
        qrRepository.save(qrData);
    }

    public ApiResponse<?> getAttendance(AttendanceRequestDto attendanceRequestDto){

        Long userId = attendanceRequestDto.getUserId();
        Integer month = attendanceRequestDto.getMonth();
        Integer year = attendanceRequestDto.getYear();
        String workType = attendanceRequestDto.getWorkType();
        String sortBy = attendanceRequestDto.getSortBy();
        String sortDirection = attendanceRequestDto.getSortDirection();
        int page = attendanceRequestDto.getPage();
        int size = attendanceRequestDto.getSize();

        // Set default values to null if they are 0 or not provided
        if (userId != null && userId == 0) userId = null;
        if (month != null && month == 0) month = null;
        if (year != null && year == 0) year = null;
        if (workType != null && workType.equals("string")) workType = null;
        if (sortBy != null && sortBy.equals("string")) sortBy = null;
        if (sortDirection != null && sortDirection.equals("string")) sortDirection = null;
        // If page or size is not provided, set them to a default value
        if (size == 0) size = 10;

        int offset = page * size;
        List<AttendanceResponseDto> records = attendanceCustomRepository.findAttendanceByCriteria(
                userId, month, year, workType, sortBy, sortDirection, size, offset);

        // Calculate pagination metadata (totalElements and totalPages)
        long totalElements = attendanceCustomRepository.countAttendanceByCriteria(userId, month, year, workType);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        // Return paged response
        PagedResponseDto<?> pagedResponseDto = PagedResponseDto.<AttendanceResponseDto>builder()
                .content(records)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .last(page == totalPages - 1)
                .build();
//        List<MyAttendanceResponseDto> attendanceResponses = new ArrayList<>();
//
//        for(Attendance attendance : attendances){
//            log.info(attendance.getWorkType());
//            MyAttendanceResponseDto myAttendanceResponseDto = new MyAttendanceResponseDto();
//            myAttendanceResponseDto.setCheckIn(attendance.getCheckin());
//            myAttendanceResponseDto.setCheckOut(attendance.getCheckout());
//            myAttendanceResponseDto.setDate(attendance.getDate());
//            myAttendanceResponseDto.setWorkType(attendance.getWorkType());

//            attendanceResponses.add(myAttendanceResponseDto);
//        }

        return ResponseUtil.getSuccessResponse(pagedResponseDto,"Successfully Fetched Attendance.");
    }

    public ApiResponse<?> getOwnAttendanceByMonth(int month, int year){

        Long userId =getCurrentUserId();
        List<Attendance> attendances = attendanceRepository.findAllByUserIdAndMonthAndYear(userId, month, year);
        List<MyAttendanceResponseDto> attendanceResponses = new ArrayList<>();

        for(Attendance attendance : attendances){
            MyAttendanceResponseDto myAttendanceResponseDto = new MyAttendanceResponseDto();
            myAttendanceResponseDto.setCheckIn(attendance.getCheckin());
            myAttendanceResponseDto.setCheckOut(attendance.getCheckout());
            myAttendanceResponseDto.setDate(attendance.getDate());
            myAttendanceResponseDto.setWorkType(attendance.getWorkType());

            attendanceResponses.add(myAttendanceResponseDto);
        }

        return ResponseUtil.getSuccessResponse(attendanceResponses,"Successfully Fetched Attendance.");
    }

    private Long getCurrentUserId() {
        // Get the authentication from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the principal, which will be the UserDetails object
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            UserInfo userInfo = userInfoRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return userInfo.getId();
        }
        throw new IllegalStateException("User is not authenticated.");
    }
}