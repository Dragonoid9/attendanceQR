package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceRequestDto {

    private Long userId;
    private Integer month;
    private Integer year;
    private String workType;
    private String sortBy = "date";  // Default value is "date"
    private String sortDirection = "ASC";  // Default value is "ASC"
    private int page = 0;  // Default value is 0
    private int size = 10;  // Default value is 10

}
