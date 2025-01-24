package com.cosmotechintl.AttendanceSystem.service.impl;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceExportRequestDto;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import com.cosmotechintl.AttendanceSystem.mapper.AttendanceCustomRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.service.ExcelService;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import com.cosmotechintl.AttendanceSystem.utility.ExcelUtility;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExcelServiceImpl implements ExcelService {

    private final AttendanceCustomRepository attendanceCustomRepository;
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;

    ExcelServiceImpl(AttendanceCustomRepository attendanceCustomRepository, UserService userService, UserInfoRepository userInfoRepository) {
        this.attendanceCustomRepository = attendanceCustomRepository;
        this.userService = userService;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public ResponseEntity<?> attendanceExportToExcel(AttendanceExportRequestDto attendanceRequestDto) {

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

        if (records.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No attendance records found");
        }

        // Generate Excel file
        String[] headers = {"S.N", "Username", "Check In", "Check Out", "Date", "Work Type"};

        // Create DateTimeFormatter for 12-hour time format (e.g., "02:30 PM")
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        // Prepare data for the Excel file
        List<List<Object>> excelData = new ArrayList<>();
        for (AttendanceResponseDto attendance : records) {
            List<Object> rowData = new ArrayList<>();
            rowData.add(attendance.getUsername());
            rowData.add(timeFormatter.format(attendance.getCheckIn()));
            rowData.add(attendance.getCheckOut() != null ? timeFormatter.format(attendance.getCheckOut()) : "");
            rowData.add(attendance.getDate().toString());
            rowData.add(attendance.getWorkType());
            excelData.add(rowData);
        }

        ByteArrayInputStream excelStream = ExcelUtility.dataToExcel("Attendance Report", headers, excelData);

        // Format current date for file name
        String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "attendance_report_" + formattedDate + ".xlsx";

        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=" + fileName);
        headersResponse.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


        return ResponseEntity.ok().headers(headersResponse).body(new InputStreamResource(excelStream));
    }

    @Override
    public ResponseEntity<?> exportEmptyUserExcelTemplate() {

        String[] headers = {"S.N", "Username", "Email", "Password", "Phone Number", "Address", "Salary", "Department", "Date of Birth", "Hire Date", "Status", "Roles"};

        List<List<Object>> excelData = new ArrayList<>();

        ByteArrayInputStream excelStream = ExcelUtility.dataToExcel("UserInfo", headers, excelData);


        // Return response
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.add("Content-Disposition", "attachment; filename=users_template.xlsx");
        headersResponse.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


        return ResponseEntity.ok().headers(headersResponse).body(new InputStreamResource(excelStream));

    }

    @Override
    public ResponseEntity<?> exportEmptyUserExcelTemplateFromStorage() {
        String path = "D:/CosmoIntl Intern Period ko Kaam Haru/2025-01-02/AttendanceSystem/storage/users_template.xlsx";

        File file = new File(path);
        // Check if the file exists
        if (!file.exists()) {
            // Return a bad request if the file doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Template file not found");
        }

        // Try to read the file and return it as an InputStream
        FileInputStream fileInputStream = null;

        // Try to read the file and return it as an InputStream
        try {
            fileInputStream = new FileInputStream(file);
            // Return the file as an InputStreamResource
            InputStreamResource resource = new InputStreamResource(fileInputStream);
            // Return response
            HttpHeaders headersResponse = new HttpHeaders();
            headersResponse.add("Content-Disposition", "attachment; filename=users_template.xlsx");
            headersResponse.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");


            return ResponseEntity.ok().headers(headersResponse).body(resource);

        } catch (IOException e) {
            // Handle exceptions related to reading the file
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading the template file");
        }
    }


    @Override
    public ApiResponse<?> importUsersFromExcel(MultipartFile file) {
        try {
            // Validate the file type
            if (!file.getOriginalFilename().endsWith(".xlsx")) {
                return ResponseUtil.getFailureResponse("Invalid Excel File", HttpStatus.BAD_REQUEST);
            }

            // Parse the Excel file
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<String> errorRow = new ArrayList<>();
            int successCount = 0;
            int duplicateCount = 0;

            for (Row row : sheet) {
                // Skip header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                try {
                    // Extract user details from the row
                    String username = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String password = row.getCell(3).getStringCellValue();
                    String phoneNumber = row.getCell(4).getStringCellValue();
                    String address = row.getCell(5).getStringCellValue();
                    double salary = row.getCell(6).getNumericCellValue();
                    String department = row.getCell(7).getStringCellValue();

                    // Extract the Date from Excel
                    Date dobDate = row.getCell(8).getDateCellValue();
                    Date hireDateValue = row.getCell(9).getDateCellValue();

                    // Convert java.util.Date to LocalDate
                    LocalDate dateOfBirth = dobDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    LocalDate hireDate = hireDateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    String status = row.getCell(10).getStringCellValue();
                    String rolesString = row.getCell(11).getStringCellValue();

                    // Map roles to a list
                    List<String> roles = Stream.of(rolesString.split(",")).map(role -> "ROLE_" + role.trim()).collect(Collectors.toList());

                    // Check for duplicates and skip if exists
                    if (userInfoRepository.existsByUsername(username) || userInfoRepository.existsByEmail(email)) {
                        duplicateCount++;
                        continue;
                    }

                    // Create a UserRequestDTO and call the saveUser method
                    UserRequestDTO userRequestDTO = new UserRequestDTO(username, email, password, phoneNumber, address, salary, department, dateOfBirth, hireDate, status, roles);

                    userService.saveUser(userRequestDTO);
                    successCount++;
                } catch (Exception e) {
                    // Capture the row causing the error
                    errorRow.add("Row " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    break;
                }
            }

            workbook.close();
            // If any errors are encountered, halt and report
            if (!errorRow.isEmpty()) {
                String errorMessage = "Import halted due to errors. Errors encountered in the following rows:\n" + String.join("\n", errorRow);
                return ResponseUtil.getFailureResponse(errorMessage, HttpStatus.BAD_REQUEST);
            }

            // Prepare a response with success and failure details
            String message = "Import completed. Successfully imported: " + successCount + ". Duplicate entries: " + duplicateCount;
            return ResponseUtil.getSuccessResponse(message);

        } catch (Exception e) {
            return ResponseUtil.getFailureResponse("Error importing users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
