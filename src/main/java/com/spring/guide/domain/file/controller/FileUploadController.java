package com.spring.guide.domain.file.controller;

import com.spring.guide.domain.file.domain.Print;
import com.spring.guide.domain.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping("/file")
    public String handleFileUpload(@RequestBody final Print print) {
        fileUploadService.upload(print);
        return "File uploaded successfully!";
    }
}
