package com.spring.guide.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.guide.domain.file.domain.Print;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    public void uploadMultipleFiles(final List<Print> prints, final List<MultipartFile> files) {
        if (prints.size() != files.size()) {
            throw new IllegalArgumentException("파일과 메타데이터의 개수가 일치하지 않습니다.");
        }

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            Print print = prints.get(i);

            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try {
                // S3에 파일 업로드
                final PutObjectRequest putObjectRequest = new PutObjectRequest(
                        BUCKET_NAME,
                        print.filePath(),
                        file.getInputStream(),
                        objectMetadata
                );
                s3Client.putObject(putObjectRequest);

                // 메타데이터 저장
                String sql = "INSERT INTO print (id, print_order, file_path, created_at) VALUES (?, ?, ?, ?)";
                jdbcTemplate.update(sql, UUID.randomUUID(), print.printOrder(), file.getOriginalFilename(), LocalDateTime.now());
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 중 오류 발생", e);
            }
        }
    }
}
