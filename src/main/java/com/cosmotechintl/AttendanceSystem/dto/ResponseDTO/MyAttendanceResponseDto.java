package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyAttendanceResponseDto {

    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss a", timezone = "Asia/Kathmandu")
    private LocalDateTime checkIn;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss a", timezone = "Asia/Kathmandu")
    private LocalDateTime checkOut;
    private LocalDate date;
    private String workType;
}
