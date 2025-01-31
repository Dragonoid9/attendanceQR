package com.cosmotechintl.AttendanceSystem.dto.RequestDTO;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceExportRequestDTO {

    private String username;
    private Integer month;
    private Integer year;
    private String workType;
    @Builder.Default
    private String sortBy = "date";
    @Builder.Default// Default value is "date"
    private String sortDirection = "ASC";  // Default value is "ASC"
}
