package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import com.cosmotechintl.AttendanceSystem.entity.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private double salary;
    private String department;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private String status;
    private List<UserRole> roles;
}
