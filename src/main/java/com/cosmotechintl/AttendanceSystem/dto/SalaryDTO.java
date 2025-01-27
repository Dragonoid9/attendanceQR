package com.cosmotechintl.AttendanceSystem.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryDTO {

    private String username;
    private double salary;
    private String date;
    private double totalHours;
}
