package com.cosmotechintl.AttendanceSystem.utility;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVPrinter;

import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtility {
    // Method to write CSV with header and data
    public static ByteArrayInputStream writeToCSV(String[] header, List<String[]> data) {
        // Create a ByteArrayOutputStream to write the CSV file content
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Writer writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream))) {

            // Use the builder API for CSVFormat
            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setHeader(header)
                    .build();

            // Create CSVPrinter with the header and data
            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                for (String[] record : data) {
                    csvPrinter.printRecord((Object[]) record);
                }
            }

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV file", e);
        }
    }

    // Method to read CSV file and return records as a list of string arrays
    public static List<String[]> readCSV(MultipartFile file) throws IOException {
        List<String[]> records = new ArrayList<>();

        // Use the builder API for CSVFormat with first record as header
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setSkipHeaderRecord(true) // Skip the header row
                .setHeader() // Read headers dynamically from the file
                .build();

        // Parse the CSV file
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(file.getInputStream()), csvFormat)) {
            for (CSVRecord csvRecord : csvParser) {
                String[] record = new String[csvRecord.size()];
                for (int i = 0; i < csvRecord.size(); i++) {
                    record[i] = csvRecord.get(i);
                }
                records.add(record);
            }
        }

        return records;
    }
}
