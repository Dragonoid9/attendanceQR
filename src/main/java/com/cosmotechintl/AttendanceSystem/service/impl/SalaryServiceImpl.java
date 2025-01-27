package com.cosmotechintl.AttendanceSystem.service.impl;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.SalaryRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.PagedResponseDto;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.SalaryResponseDTO;
import com.cosmotechintl.AttendanceSystem.dto.SalaryDTO;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.mapper.SalaryCustomRepository;
import com.cosmotechintl.AttendanceSystem.service.SalaryService;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {

    private final SalaryCustomRepository salaryCustomRepository;

    SalaryServiceImpl(SalaryCustomRepository salaryCustomRepository) {
        this.salaryCustomRepository = salaryCustomRepository;
    }

    public ApiResponse<?> getMonthlySalary(SalaryRequestDTO salaryRequestDto){

        String username = salaryRequestDto.getUsername();
        Integer month = salaryRequestDto.getMonth();
        Integer year = salaryRequestDto.getYear();
        String workType = salaryRequestDto.getWorkType();
        String sortBy = salaryRequestDto.getSortBy();
        String sortDirection = salaryRequestDto.getSortDirection();
        int page = salaryRequestDto.getPage();
        int size = salaryRequestDto.getSize();

        // Set default values to null if they are 0 or not provided
        if (month != null && month == 0) month = null;
        if (year != null && year == 0) year = null;
        if(username != null && username.equals("string")) username = null;
        if (workType != null && workType.equals("string")) workType = null;
        if (sortBy != null && sortBy.equals("string")) sortBy = null;
        if (sortDirection != null && sortDirection.equals("string")) sortDirection = null;
        // If page or size is not provided, set them to a default value
        if (size == 0) size = 10;
        int offset = page * size;

        List<SalaryDTO> records = salaryCustomRepository.findAccruedSalaryByMonthAndYear(username, month, year, workType, sortBy, sortDirection, size, offset);
        // Calculate pagination metadata (totalElements and totalPages)
        Long totalElements = salaryCustomRepository.countAccruedSalaryByMonthAndYear(username, month, year, workType);

        if (totalElements == null || totalElements == 0) {
            throw new ResourceNotFoundException("No data found ");
        }
        long total = (totalElements != null) ? totalElements : 0L;

        int totalPages = (total > 0) ? (int) Math.ceil((double) total / size) : 0;

        List<SalaryResponseDTO> salaryResponseDTOS = new ArrayList<>();
        for(SalaryDTO salaryDTO : records){
            double hourlyRate= salaryDTO.getSalary()/(24*8);
            double accruedAmount = salaryDTO.getTotalHours()*hourlyRate;

            SalaryResponseDTO salaryResponseDTO = SalaryResponseDTO.builder()
                    .username(salaryDTO.getUsername())
                    .date(salaryDTO.getDate())
                    .salary(salaryDTO.getSalary())
                    .accruedAmount(accruedAmount)
                    .totalHours(salaryDTO.getTotalHours())
                    .build();

            salaryResponseDTOS.add(salaryResponseDTO);
        }

        PagedResponseDto<?> pagedResponseDto = PagedResponseDto.<SalaryResponseDTO>builder()
                .content(salaryResponseDTOS)
                .page(page)
                .size(size)
                .totalElements(total)
                .totalPages(totalPages)
                .last(page == totalPages - 1)
                .build();
        return ResponseUtil.getSuccessResponse(pagedResponseDto, "Successfully Fetched Salary.");
    }

    private String calculateHoursWorked(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn != null && checkOut != null) {
            // Calculate the duration between check-in and check-out
            Duration duration = Duration.between(checkIn, checkOut);

            // Get hours and minutes worked
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            // Return formatted string
            return String.format("%02d:%02d", hours, minutes);
        }
        return "00:00";  // If either checkIn or checkOut is null, return 00:00
    }
}
