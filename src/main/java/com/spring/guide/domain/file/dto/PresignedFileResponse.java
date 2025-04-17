package com.spring.guide.domain.file.dto;

public record PresignedFileResponse(
        String filename,
        String key,
        String presignedUrl
) {
}
