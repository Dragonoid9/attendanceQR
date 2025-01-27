package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalaryRequestDTO {

    private String username;
    private Integer month;
    private Integer year;
    private String workType;
    @Builder.Default
    private String sortBy = "date";
    @Builder.Default// Default value is "date"
    private String sortDirection = "ASC";  // Default value is "ASC"
    @Builder.Default
    private int page = 0;  // Default value is 0
    @Builder.Default
    private int size = 10;  // Default value is 10
}
