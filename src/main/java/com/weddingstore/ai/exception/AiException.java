package com.weddingstore.ai.exception;

import com.weddingstore.common.exception.ErrorCode;

public class AiException extends RuntimeException {

    private final ErrorCode errorCode;

    public AiException(ErrorCode errorCode,
                       String message,
                       Throwable cause) {

        super(message, cause);

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}