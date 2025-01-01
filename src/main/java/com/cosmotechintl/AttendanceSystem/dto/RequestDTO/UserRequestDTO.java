package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private List<String> roles;

}
