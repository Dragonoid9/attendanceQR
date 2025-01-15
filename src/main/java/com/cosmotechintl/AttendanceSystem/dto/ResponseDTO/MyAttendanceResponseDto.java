package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyAttendanceResponseDto {

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDate date;
    private String workType;
}
