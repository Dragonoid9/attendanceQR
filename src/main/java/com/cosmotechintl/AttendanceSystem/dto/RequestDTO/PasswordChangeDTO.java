package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDTO {

    private String username;
    private String currentPassword;
    private String newPassword;
}
