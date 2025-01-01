package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {

    String accessToken;
    String refreshToken;
}
