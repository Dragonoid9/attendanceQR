package com.cosmotechintl.AttendanceSystem.utility;

import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AttendanceResponseDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtility {


    public static ByteArrayInputStream dataToExcel(List<AttendanceResponseDto> attendances, String sheetName, String[] header) {

        try(Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){

            Sheet sheet = workbook.createSheet(sheetName);
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
            }

            // Fill data rows
            for (int i = 0; i < attendances.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                AttendanceResponseDto attendance = attendances.get(i);

                // Assuming AttendanceResponseDto has appropriate getter methods
                dataRow.createCell(0).setCellValue(attendance.getUsername());
                dataRow.createCell(1).setCellValue(attendance.getCheckIn().toString());
                dataRow.createCell(2).setCellValue(attendance.getCheckOut() != null ? attendance.getCheckOut().toString() : "");
                dataRow.createCell(3).setCellValue(attendance.getDate().toString());
                dataRow.createCell(4).setCellValue(attendance.getWorkType());
            }
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}
