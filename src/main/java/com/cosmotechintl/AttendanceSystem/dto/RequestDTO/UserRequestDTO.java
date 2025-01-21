package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private double salary;
    private String department;
    private LocalDate dateOfBirth;
    private LocalDate hireDate;
    private String status;
    private List<String> roles;

}
