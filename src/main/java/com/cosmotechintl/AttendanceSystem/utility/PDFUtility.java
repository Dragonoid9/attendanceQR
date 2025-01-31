package com.cosmotechintl.AttendanceSystem.utility;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class PDFUtility {

    public static ByteArrayInputStream dataToPDF(String pTitle, String[] headers, List<List<Object>> data) {

        // Create a ByteArrayOutputStream to hold the PDF content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        try {
            // Create Document and PdfWriter instances
            PdfWriter.getInstance(document, outputStream);

            // Open the document to add content
            document.open();

            // Set up a title (optional)
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph title = new Paragraph(pTitle, font);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            // Add a custom gap after the title
            document.add(Chunk.NEWLINE); // Adds a new line
            // Adding some space after the title
            title.setSpacingAfter(20f);  // This adds space after the title (you can adjust the value)

            // Create the table with the number of columns equal to the length of headers
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100f);

            // Calculate maximum width for the SN column based on the largest serial number
            int maxSNLength = String.valueOf(data.size()).length();
            float snColumnWidth = maxSNLength * 0.3f;  // Adjust factor to control column width dynamically

            // Set the width of the SN column dynamically, and leave the others with default widths
            float[] columnWidths = new float[headers.length];
            columnWidths[0] = snColumnWidth;  // SN column width (dynamic)
            for (int i = 1; i < headers.length; i++) {
                columnWidths[i] = 1.5f;  // Default width for the other columns

//                float maxDataLength = getMaxColumnDataLength(i, data); for the dynamic width according to the datasize
//                columnWidths[i] = maxDataLength * 0.5f;  // Adjust factor to scale the column width
            }

            table.setWidths(columnWidths);

            // Write the table headers
            writeTableHeader(table, headers);

            // Write the table data
            writeTableData(table, data);

            // Add table to the document
            document.add(table);

        } catch (DocumentException e) {
            throw new RuntimeException("Error creating PDF document", e);
        }
        // Close the document and return the ByteArrayInputStream
        document.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void writeTableHeader(PdfPTable table, String[] headers) {

        // Create a bold and larger font for the header
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headerFont.setSize(12); // You can change the size here to make it larger than the table data


        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            table.addCell(headerCell);
        }
    }

    private static void writeTableData(PdfPTable table, List<List<Object>> data) {

        // Create a regular font for the data
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA);
        dataFont.setSize(11); // Regular size for table data

        int s = 1; // Starting serial number
        for (List<Object> rowData : data) {
            table.addCell(String.valueOf(s));
            for (Object cell : rowData) {
                table.addCell(cell != null ? cell.toString() : "");
            }
            s++;
        }
    }

    //This is only used to make the dynamic width for the colum having different data sizes.
    private static float getMaxColumnDataLength(int columnIndex, List<List<Object>> data) {
        float maxLength = 0;
        for (List<Object> rowData : data) {
            if (rowData.size() > columnIndex) {
                String cellData = String.valueOf(rowData.get(columnIndex));
                maxLength = Math.max(maxLength, cellData.length());
            }
        }
        return maxLength;
    }
}
