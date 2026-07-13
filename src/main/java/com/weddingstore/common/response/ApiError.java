package com.weddingstore.common.response;

import com.weddingstore.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiError {

    private ErrorCode code;

    private String message;

    private LocalDateTime timestamp;
}