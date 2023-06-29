package com.ilario.sparkmart.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUploadUtil {

    private static boolean doesPhotosFolderExist(String parentFolderPath) {
        File parentFolder = new File(parentFolderPath);
        File[] childFiles = parentFolder.listFiles();

        if (childFiles != null) {
            for (File childFile : childFiles) {
                if (childFile.isDirectory() && childFile.getName().equals("SparkMartPhotos")) {
                    return true;
                }
            }
        }

        return false;
    }

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
        File photoDir = new File(Objects.requireNonNull(FileUploadUtil.class.getResource("/"))
                .getPath())
                .getParentFile()
                .getParentFile()
                .getParentFile();
        if(!doesPhotosFolderExist(photoDir.getAbsolutePath())) {
            File newPhotosFolder = new File(photoDir.getAbsolutePath() + File.separator + "SparkMartPhotos");
            if(!newPhotosFolder.mkdir()) {
                System.out.println(newPhotosFolder.getAbsolutePath());
                System.out.println("ERROR: Could not make photos folder!");
            }
        }
        String newFileName = removeSpecialCharacters(fileName);
        Path uploadPath = Paths.get(photoDir.getAbsolutePath() + File.separator + "SparkMartPhotos" + File.separator + uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectory(uploadPath);
        }
        try (InputStream inputStream = file.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            Path source = Paths.get(photoDir.getAbsolutePath() + File.separator + "SparkMartPhotos" + File.separator + uploadDir + File.separator + fileName);
            Files.move(source, source.resolveSibling(newFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            throw  new IOException("ERROR: Could not save file: " + fileName, ex);
        }
    }
}
