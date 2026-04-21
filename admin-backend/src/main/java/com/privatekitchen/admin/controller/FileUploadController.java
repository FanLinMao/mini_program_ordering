package com.privatekitchen.admin.controller;

import com.privatekitchen.admin.common.ApiResponse;
import com.privatekitchen.admin.service.FileStorageService;
import com.privatekitchen.admin.vo.UploadFileVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/files")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/dish-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UploadFileVO> uploadDishImage(@RequestPart("file") MultipartFile file) {
        return ApiResponse.success("上传成功", fileStorageService.uploadDishImage(file));
    }
}
