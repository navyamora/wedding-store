package com.weddingstore.storage.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {

    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required.");
        }

        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("Maximum file size is 5 MB.");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException(
                    "Only JPG, PNG and WebP images are allowed."
            );
        }
    }
}