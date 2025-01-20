package com.cosmotechintl.AttendanceSystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;

@Component
public class QRCodeGenerator {

    private static final String BASE_URL = "192.168.1.89:8080/attendance";

    public String generateQRCodeData(String apiUrl, String token) {
        try {

            Map<String, String> qrData = Map.of(
                    "baseUrl", BASE_URL,
                    "apiUrl", apiUrl,
                    "token", token
            );
            // Encode the token as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(qrData);

        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code data", e);
        }
    }

    public BufferedImage generateQRCodeImage(String qrCodeData) throws Exception {
        int size = 250; // Size of the QR code
        MultiFormatWriter qrCodeWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, size, size);

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size, size);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (bitMatrix.get(i, j)) {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                } else {
                    image.setRGB(i, j, Color.WHITE.getRGB());
                }
            }
        }
        return image;
    }

    public void saveQRCodeImage(String qrCodeData, String filePath) throws Exception {
        BufferedImage qrImage = generateQRCodeImage(qrCodeData);
        File file = new File(filePath);
        ImageIO.write(qrImage, "PNG", file);
    }
}
