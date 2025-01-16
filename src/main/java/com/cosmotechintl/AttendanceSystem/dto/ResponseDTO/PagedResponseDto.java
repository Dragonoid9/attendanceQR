package com.cosmotechintl.AttendanceSystem.dto.ResponseDTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponseDto<T> {
    private List<T> content; // The list of records
    private int page;        // Current page number (0-based)
    private int size;        // Page size
    private long totalElements; // Total number of elements
    private int totalPages;  // Total number of pages
    private boolean last;    // Whether this is the last page
}
