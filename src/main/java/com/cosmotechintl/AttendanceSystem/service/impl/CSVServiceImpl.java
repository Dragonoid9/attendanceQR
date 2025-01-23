package com.cosmotechintl.AttendanceSystem.service.impl;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.mapper.AttendanceCustomRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.service.CSVService;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import com.cosmotechintl.AttendanceSystem.utility.CSVUtility;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CSVServiceImpl implements CSVService {

    private final AttendanceCustomRepository attendanceCustomRepository;
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;

    CSVServiceImpl(AttendanceCustomRepository attendanceCustomRepository, UserService userService, UserInfoRepository userInfoRepository) {
        this.attendanceCustomRepository = attendanceCustomRepository;
        this.userService = userService;
        this.userInfoRepository = userInfoRepository;
    }


    @Override
    public ResponseEntity<?> attendanceExportToCSV(AttendanceExportRequestDto attendanceRequestDto) {

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

        List<AttendanceResponseDto> records = attendanceCustomRepository.findAttendanceByCriteriaExcel(username, month, year, workType, sortBy, sortDirection);

        String[] headers = {"S.N", "Username", "Check In", "Check Out", "Date", "Work Type", "Hours Worked"};

        // Create DateTimeFormatter for 12-hour time format (e.g., "02:30 PM")
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Prepare data for the CSV file
        List<String[]> data = new ArrayList<>();
        int serialNumber = 1;

        for (AttendanceResponseDto attendance : records) {
            List<String> rowData = new ArrayList<>();
            // Add user-related data
            rowData.add(String.valueOf(serialNumber++));  // Add serial number
            rowData.add(attendance.getUsername());
            rowData.add(timeFormatter.format(attendance.getCheckIn()));
            rowData.add(attendance.getCheckOut() != null ? timeFormatter.format(attendance.getCheckOut()) : "");
            rowData.add(attendance.getDate().toString());
            rowData.add(attendance.getWorkType());

            // Calculate hours worked
            String hoursWorked = calculateHoursWorked(attendance.getCheckIn(), attendance.getCheckOut());
            rowData.add(hoursWorked);

            // Add the row to the data list
            data.add(rowData.toArray(new String[0])); // Convert list to array and add to CSV data
        }

        ByteArrayInputStream csvStream = CSVUtility.writeToCSV(headers, data);

        // Format current date for file name
        String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "attendance_report_" + formattedDate + ".csv";

        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity.ok().headers(headersResponse).body(new InputStreamResource(csvStream));
    }

    @Override
    public ResponseEntity<?> exportEmptyUserCSVTemplate() {
        String[] headers = {"S.N", "Username", "Email", "Password", "Phone Number", "Address", "Salary", "Department", "Date of Birth", "Hire Date", "Status", "Roles"};
        List<String[]> data = new ArrayList<>();

        ByteArrayInputStream csvTemplate = CSVUtility.writeToCSV(headers, data);

        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=users_template.csv");

        return ResponseEntity.ok().headers(headersResponse).body(new InputStreamResource(csvTemplate));

    }

    @Override
    public ApiResponse<?> importUsersFromCSV(MultipartFile file) {

        try {
            // Validate the file type
            if (!file.getOriginalFilename().endsWith(".csv")) {
                return ResponseUtil.getFailureResponse("Invalid CSV File", HttpStatus.BAD_REQUEST);
            }
            List<String[]> records = CSVUtility.readCSV(file);
            List<String> errorRow = new ArrayList<>();
            int successCount = 0;
            int duplicateCount = 0;

            for (int i = 0; i < records.size(); i++) {
                String[] record = records.get(i);
                try {
                    // Ensure record has all required fields
                    if (record.length < 12) {
                        errorRow.add("Row " + (i + 1) + ": Insufficient columns.");
                        // Halt the process and return the error immediately
                        return ResponseUtil.getFailureResponse("Import halted due to errors. Errors encountered in the following rows:\n" + String.join("\n", errorRow), HttpStatus.BAD_REQUEST);
                    }

                    // Extract fields from the CSV record
                    String username = record[1];
                    String email = record[2];
                    String password = record[3];
                    String phoneNumber = record[4];
                    String address = record[5];
                    double salary = Double.parseDouble(record[6]);
                    String department = record[7];

                    // Parse dates
                    LocalDate dateOfBirth = LocalDate.parse(record[8]);
                    LocalDate hireDate = LocalDate.parse(record[9]);

                    String status = record[10];
                    String rolesString = record[11];

                    // Map roles to a list
                    List<String> roles = Stream.of(rolesString.split(","))
                            .map(role -> "ROLE_" + role.trim())
                            .collect(Collectors.toList());

                    // Check for duplicates and skip if exists
                    if (userInfoRepository.existsByUsername(username) || userInfoRepository.existsByEmail(email)) {
                        duplicateCount++;
                        continue;
                    }

                    // Create UserRequestDTO and call saveUser method
                    UserRequestDTO userRequestDTO = new UserRequestDTO(username, email, password, phoneNumber,
                            address, salary, department, dateOfBirth,
                            hireDate, status, roles);
                    userService.saveUser(userRequestDTO);
                    successCount++;

                } catch (Exception e) {
                    // Halt the process on error and return the error details
                    errorRow.add("Row " + (i + 1) + ": " + e.getMessage());
                    return ResponseUtil.getFailureResponse("Import halted due to errors. Errors encountered in the following rows:\n" + String.join("\n", errorRow), HttpStatus.BAD_REQUEST);
                }
            }
            // Success response
            String message = "Import completed. Successfully imported: " + successCount + " users. Duplicate entries: " + duplicateCount;
            return ResponseUtil.getSuccessResponse(message);

        } catch (Exception e) {
            // General error handling
            return ResponseUtil.getFailureResponse("Error importing users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to calculate hours worked
    private String calculateHoursWorked(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn != null && checkOut != null) {
            // Calculate the duration between check-in and check-out
            Duration duration = Duration.between(checkIn, checkOut);

            // Get hours and minutes worked
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            // Return formatted string
            return String.format("%02d:%02d", hours, minutes);
        }
        return "00:00";  // If either checkIn or checkOut is null, return 00:00
    }
}
