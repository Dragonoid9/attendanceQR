package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryResponseDTO {

    private String username;
    private double salary;
    private double totalHours;
    private double accruedAmount;
    private String date;
}
