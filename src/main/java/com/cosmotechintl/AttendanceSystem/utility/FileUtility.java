package com.cosmotechintl.AttendanceSystem.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUtility {

    /**
     * Saves the given MultipartFile to the specified directory.
     *
     * @param file        The file to be stored.
     * @param targetDir   The directory path where the file should be stored.
     * @return The path of the stored file.
     * @throws IOException If an error occurs during file saving.
     */
    public static String saveFile(File file, String targetDir) throws IOException {
        // Validate file
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or does not exist.");
        }

        // Ensure the target directory exists
        Path directoryPath = Path.of(targetDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
        }
        // Create a unique file name to avoid overwriting existing files
        String originalFileName = file.getName();
        String uniqueFileName = System.currentTimeMillis() + "_" + (originalFileName != null ? originalFileName : "uploaded_file");

        // Define the file path
        Path targetPath = directoryPath.resolve(uniqueFileName);

        // Copy the file content to the target location
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetDir + uniqueFileName; // Return the absolute path of the saved file
    }
}
