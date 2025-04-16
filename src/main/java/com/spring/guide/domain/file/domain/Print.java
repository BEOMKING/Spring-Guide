package com.spring.guide.domain.file.domain;

import java.time.LocalDate;

public record Print(
        String id,
        int printOrder,
        String filePath,
        LocalDate createdAt
) {
}
