package com.weddingstore.storage.service;

import com.weddingstore.common.config.properties.S3Properties;
import com.weddingstore.storage.dto.UploadResponse;
import com.weddingstore.storage.validator.FileValidator;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private final FileValidator fileValidator;

    public S3StorageService(S3Client s3Client,
                            S3Properties s3Properties,
                            FileValidator fileValidator) {
        this.s3Client = s3Client;
        this.s3Properties = s3Properties;
        this.fileValidator = fileValidator;
    }

    @Override
    public UploadResponse upload(MultipartFile file, String folder) {

        try {

            String extension = getExtension(file.getOriginalFilename());

            String fileName = UUID.randomUUID() + "." + extension;

            LocalDate today = LocalDate.now();

            String key = folder + "/"
                    + today.getYear() + "/"
                    + String.format("%02d", today.getMonthValue()) + "/"
                    + fileName;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            String url =
                    "https://"
                            + s3Properties.getBucket()
                            + ".s3."
                            + s3Properties.getRegion()
                            + ".amazonaws.com/"
                            + key;
            System.out.println("S3 bucket = " + s3Properties.getBucket());
            System.out.println("S3 region = " + s3Properties.getRegion());

            return UploadResponse.builder()
                    .fileName(fileName)
                    .url(url)
                    .size(file.getSize())
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Unable to upload file", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        // We'll implement later
    }

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return "jpg";
        }

        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}