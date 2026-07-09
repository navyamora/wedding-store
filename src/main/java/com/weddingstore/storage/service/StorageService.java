package com.weddingstore.storage.service;

import com.weddingstore.storage.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    UploadResponse upload(MultipartFile file, String folder);

    void delete(String fileUrl);
}