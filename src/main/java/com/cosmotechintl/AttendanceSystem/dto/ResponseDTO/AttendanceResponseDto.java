package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponseDto {

    private String username;
    private Long id;
    private Long userId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private LocalDate date;
    private String workType;
}
