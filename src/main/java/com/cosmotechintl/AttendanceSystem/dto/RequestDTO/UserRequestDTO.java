package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {


    private String username;
    @Email(message ="Invalid Email,Please Enter valid Email.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{9,18}$",
            message = "Password must include uppercase, lowercase, digit, special character, and be 9-18 characters long.")
    private String password;
    @Pattern(regexp = "^(\\+977(97[4-9][0-9]{6}|98[4-9][0-9]{6})|97[4-9][0-9]{6}|98[4-9][0-9]{6}|0[1-9][0-9]{7})$",
            message = "Phone number must be valid. Format: +97797[4-9]XXXXXX, +97798[4-9]XXXXXX, 97[4-9]XXXXXX, 98[4-9]XXXXXX, or 0[1-9]X{7}.")
    private String phoneNumber;
    @NotBlank(message = "Address must not be Non and empty.")
    private String address;
    @Min(value = 17000, message = "Salary must be greater than 17000.")
    private double salary;
    @NotBlank(message = "Enter Valid department.")
    private String department;
    @Past(message = "Invalid Date of Birth.")
    private LocalDate dateOfBirth;
    @PastOrPresent(message ="Invalid Hire Date.")
    private LocalDate hireDate;
    @NotBlank(message = "Invalid Status.")
    private String status;
    @Size(min = 1, message = "At least one role must be assigned.")
    private List<String> roles;
    private MultipartFile multipartFile;

}
