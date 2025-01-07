package com.cosmotechintl.AttendanceSystem.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

@Component
public class QRCodeGenerator {

    private static final String BASE_URL = "localhost:8080/attendance";

    public String generateQRCodeData(String apiUrl, LocalDateTime expiration) {
        // Construct the QR code data URL
        String expirationStr = expiration.toString();
        return String.format("%s/%s?expiration=%s", BASE_URL, apiUrl, expirationStr);
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

    public byte[] getQRCodeImage(String qrCodeData) throws Exception {
        BufferedImage bufferedImage = generateQRCodeImage(qrCodeData);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
