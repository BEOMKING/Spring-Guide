package com.spring.guide.domain.file.controller;

import com.spring.guide.domain.file.domain.Print;
import com.spring.guide.domain.file.dto.PresignedFileResponse;
import com.spring.guide.domain.file.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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

    @PostMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadMultipleFiles(
            @RequestPart(value = "files") List<MultipartFile> files,
            @RequestPart(value = "prints") List<Print> prints
    ) {
        fileUploadService.uploadMultipleFiles(prints, files);
        return "Multiple files uploaded successfully!";
    }
//        File file = new File("/Users/qjawlsqjacks/FASOO/docs/flas/tss & massro/[T-24-03-07-033] CPU 부하/mess.txt");
//        byte[] fileBytes = Files.readAllBytes(file.toPath());

    @PostMapping("/presigned-urls")
    public List<PresignedFileResponse> getPresignedUrls(@RequestBody List<String> filenames) {
        return fileUploadService.getPresignedUrls(filenames);
    }
}
