package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QRAttendance {
    String accessToken;
    String token;
}
