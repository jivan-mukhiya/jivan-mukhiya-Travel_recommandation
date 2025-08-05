package com.texas.traveldestinationrecommendation.Implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageService {
    @Value("${image.upload.dir}")
    private String uploadDir;

    public String storeImage(MultipartFile file) {
        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String sanitizedFilename = originalFilename != null ?
                    originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_") :
                    "image";

            String filename = UUID.randomUUID() + "_" + sanitizedFilename;
            Path filePath = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    public void deleteImage(String filename) throws IOException {
        try {
            Path path = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
