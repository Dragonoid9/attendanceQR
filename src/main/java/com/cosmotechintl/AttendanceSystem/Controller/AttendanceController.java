package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.AttendanceRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getAttendanceQR")
    public ApiResponse<?> getAttendanceQR() {
        return attendanceService.getAttendanceQR();
    }


    @PostMapping("/checkIn")
    public ApiResponse<?> checkInUser(@RequestBody QRAttendance attendance) {

        return attendanceService.checkIn(attendance);
    }

    @PostMapping("/checkOut")
    public ApiResponse<?> checkOutUser(@RequestBody QRAttendance attendance) {
        return attendanceService.checkOut(attendance);
    }


    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/userAttendance")
    public ApiResponse<?> getUserAttendance(@RequestBody AttendanceRequestDTO attendanceRequestDto) {
        return attendanceService.getAttendance(attendanceRequestDto);
    }

    @PreAuthorize("hasAnyRole('Employee', 'Intern')")
    @GetMapping("/ownAttendanceByMonth")
    public ApiResponse<?> getOwnAttendanceByMonth(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return attendanceService.getOwnAttendanceByMonth(month, year);
    }
}
