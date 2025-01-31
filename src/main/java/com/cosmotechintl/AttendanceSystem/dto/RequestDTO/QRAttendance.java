package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QRAttendance {
    @NotBlank(message = "Token cannot be Null or Empty")
    String accessToken;
    @NotBlank(message = "QrToken cannot be Null or Empty")
    String token;
    @NotBlank(message = "WorkType cannot be Null or Empty")
    String workType;
}
