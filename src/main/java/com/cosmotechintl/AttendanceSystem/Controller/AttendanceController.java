package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/getQR")
    public ApiResponse<?> getAttendance() {

        return attendanceService.generateQR();
    }
    @GetMapping("/checkIn")
    public ApiResponse<?> checkInUser(@RequestParam String accessToken,@RequestParam String expiration){

        return attendanceService.checkIn(accessToken, expiration);
    }
    @GetMapping("/checkOut")
    public ApiResponse<?> checkOutUser(@RequestParam String accessToken,@RequestParam String expiration){
        return attendanceService.checkOut(accessToken, expiration);
    }
}
