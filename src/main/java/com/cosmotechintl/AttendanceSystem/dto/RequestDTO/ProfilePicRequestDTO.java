package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfilePicRequestDTO {

    private String username;
    private MultipartFile file;
}
