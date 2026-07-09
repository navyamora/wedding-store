package com.weddingstore.storage.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadResponse {

    private String fileName;
    private String url;
    private Long size;
}