package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "Username cannot be Null or Empty.")
    private String username;
    @NotBlank(message = "Password cannot be Null or Empty.")
    private String password;

}
