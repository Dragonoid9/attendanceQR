package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;



import com.cosmotechintl.AttendanceSystem.entity.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private String email;
    private List<UserRole> roles;
}
