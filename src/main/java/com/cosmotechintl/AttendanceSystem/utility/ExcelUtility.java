package com.cosmotechintl.AttendanceSystem.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtility {


    public static ByteArrayInputStream dataToExcel(String sheetName, String[] header, List<List<Object>> data) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {

            Sheet sheet = workbook.createSheet(sheetName);
            // Create header row
            Row headerRow = sheet.createRow(0);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true); // Set header font to bold
            headerFont.setFontHeightInPoints((short) 14); // Set header font size

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont); // Apply bold font to header style

            for (int i = 0; i < header.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(headerCellStyle);
            }
            
            //Create data row
            // Create data rows with regular font size
            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 11); // Set row data font size

            CellStyle dataCellStyle = workbook.createCellStyle();
            dataCellStyle.setFont(dataFont); // Apply normal font to data style

            for (int i = 0; i < data.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                List<Object> rowData = data.get(i);

                // Add serial number as the first column in each row
                dataRow.createCell(0).setCellValue(i + 1); // Serial number (starting from 1)
                for (int j = 0; j < rowData.size(); j++) {
                    Cell cell = dataRow.createCell(j + 1);// Data starts from the second column (index 1)
                    cell.setCellValue(rowData.get(j) != null ? rowData.get(j).toString() : "");
                    cell.setCellStyle(dataCellStyle); // Apply data style to each cell in row
                }
            }
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}
