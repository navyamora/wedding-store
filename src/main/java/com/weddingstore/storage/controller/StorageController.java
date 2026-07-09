package com.weddingstore.storage.controller;

import com.weddingstore.common.response.ApiResponse;
import com.weddingstore.storage.dto.UploadResponse;
import com.weddingstore.storage.service.StorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ApiResponse<UploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "products") String folder
    ) {
        UploadResponse response = storageService.upload(file, folder);

        return ApiResponse.<UploadResponse>builder()
                .success(true)
                .message("Upload successful")
                .data(response)
                .build();
    }

}