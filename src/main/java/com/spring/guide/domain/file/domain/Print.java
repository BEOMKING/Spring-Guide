package com.spring.guide.domain.file.domain;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record Print(
        @NotNull
        String id,
        int printOrder,
        String filePath,
        LocalDate createdAt
) {
}
