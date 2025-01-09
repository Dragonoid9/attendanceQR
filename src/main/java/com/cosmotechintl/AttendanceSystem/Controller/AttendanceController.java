package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.QRAttendance;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getQR")
    public ResponseEntity<?> getAttendance() {

        return attendanceService.getQR();
    }
    @PostMapping("/checkIn")
    public ApiResponse<?> checkInUser(@RequestBody QRAttendance attendance) {

        return attendanceService.checkIn(attendance);
    }
    @PostMapping("/checkOut")
    public ApiResponse<?> checkOutUser(@RequestBody QRAttendance attendance ) {
        return attendanceService.checkOut(attendance);
    }
}
