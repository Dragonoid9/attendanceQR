package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {

    @NotBlank(message = "Username cannot be Null or Empty")
    private String username;
    @NotBlank(message = "Username cannot be Null or Empty")
    private String currentPassword;
    @NotBlank(message = "Username cannot be Null or Empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{9,18}$",
            message = "Password must include uppercase, lowercase, digit, special character, and be 9-18 characters long.")
    private String newPassword;
}
