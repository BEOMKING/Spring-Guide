package com.spring.guide.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.guide.domain.file.domain.Print;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private static final String BUCKET_NAME = "bjp-fasoo";

    private final JdbcTemplate jdbcTemplate;
    private final AmazonS3 s3Client;

    public void upload(Print print) {
        String sql = "INSERT INTO print (id, print_order, file_path, created_at) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID(), print.printOrder(), print.filePath(), LocalDateTime.now());

        s3Client.putObject(new PutObjectRequest(BUCKET_NAME, print.filePath(), new File("/Users/qjawlsqjacks/Projects/Spring-Guide/src/main/resources/schema.sql")));
    }
}
