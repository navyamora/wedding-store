package com.weddingstore.common.exception;

public enum ErrorCode {

    // General
    INTERNAL_SERVER_ERROR,
    VALIDATION_ERROR,
    RESOURCE_NOT_FOUND,
    CONFLICT,

    // Authentication
    UNAUTHORIZED,
    ACCESS_DENIED,
    INVALID_TOKEN,

    // AI
    AI_SERVICE_UNAVAILABLE,
    AI_QUOTA_EXCEEDED,
    AI_INVALID_API_KEY,

    // Storage
    FILE_UPLOAD_FAILED,
    INVALID_FILE,
    FILE_TOO_LARGE
}