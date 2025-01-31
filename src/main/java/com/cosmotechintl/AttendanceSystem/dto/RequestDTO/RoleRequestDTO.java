package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDTO {

    @NotBlank(message = "Role cannot be Null or Empty")
    private String name;
}
