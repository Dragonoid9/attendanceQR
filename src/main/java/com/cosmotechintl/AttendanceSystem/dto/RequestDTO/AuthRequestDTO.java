package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDTO {

    private String username;
    private String password;
}
