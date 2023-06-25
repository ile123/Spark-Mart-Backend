package com.ilario.sparkmart.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static String removeSpecialCharacters(String input) {
        String result = input.replace(" ", "-");
        int lastDotIndex = input.lastIndexOf('.');
        String fileExtension = input.substring(lastDotIndex + 1);
        result = result.replace("(", "_");
        result = result.replace(")", "_");
        result = result.replace("-", "_");
        result = result.replaceAll("\\.", "_");
        result = result.replaceAll("_+", "_");
        result = result.replaceAll("^-|-$", "");
        result = result.substring(0, lastDotIndex) + "." + fileExtension;
        return result;
    }
    public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        String newFileName = removeSpecialCharacters(fileName);
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }
        try (InputStream inputStream = file.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            Path source = Paths.get(uploadDir + "/" + fileName);
            Files.move(source, source.resolveSibling(newFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw  new IOException("ERROR: Could not save file: " + fileName, ex);
        }
    }
}
