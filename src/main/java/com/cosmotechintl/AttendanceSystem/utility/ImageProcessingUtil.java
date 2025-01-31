package com.cosmotechintl.AttendanceSystem.utility;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class ImageProcessingUtil {

    // Method to resize the image (for compression)
    public static void resizeImage(File inputFile, File outputFile, int width, int height) throws IOException {
        BufferedImage originalImage = Imaging.getBufferedImage(inputFile);
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        ImageIO.write(resizedImage, "png", outputFile); // You can change the format here
    }

    // Method to convert image format (e.g., PNG to JPEG)
    public static void convertImageFormat(File inputFile, File outputFile, String format) throws IOException {
        BufferedImage bufferedImage = Imaging.getBufferedImage(inputFile);
        Imaging.writeImage(bufferedImage, outputFile, ImageFormats.valueOf(format.toUpperCase()));
    }

    // Method to compress a JPEG image by reducing the quality
    public static void compressJPEG(File inputFile, File outputFile, float quality) throws IOException {
        BufferedImage bufferedImage = Imaging.getBufferedImage(inputFile);

        // Create a JPEG image writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IOException("No suitable JPEG writer found");
        }
        ImageWriter writer = writers.next();

        // Set the quality parameter
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality); // 0.0 (low quality) to 1.0 (high quality)
        }

        // Write the compressed image
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    // Method to compress a PNG image (lossless compression)
    public static void compressPNG(File inputFile, File outputFile) throws IOException {
        BufferedImage bufferedImage = Imaging.getBufferedImage(inputFile);

        // PNG compression is lossless by default
        ImageIO.write(bufferedImage, "png", outputFile);
    }

    // Method for compressing GIF images (lossless)
    public static void compressGIF(File inputFile, File outputFile) throws IOException {
        BufferedImage bufferedImage = Imaging.getBufferedImage(inputFile);
        ImageIO.write(bufferedImage, "gif", outputFile); // GIF compression is lossless by default
    }

    // Method to handle compression of different image formats
    public static void compressImage(File inputFile, File outputFile, float quality) throws IOException {
        String formatName = Imaging.guessFormat(inputFile).getName();

        switch (formatName.toLowerCase()) {
            case "jpeg":
            case "jpg":
                compressJPEG(inputFile, outputFile, quality);
                break;
            case "png":
                compressPNG(inputFile, outputFile);
                break;
            case "gif":
                compressGIF(inputFile, outputFile);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported image format: " + formatName);
        }
    }

    // Method to read and print image metadata
    public static void printImageMetadata(File imageFile) throws Exception {
        ImageMetadata metadata = Imaging.getMetadata(imageFile);
        System.out.println("Image Metadata: " + metadata);
    }
}

