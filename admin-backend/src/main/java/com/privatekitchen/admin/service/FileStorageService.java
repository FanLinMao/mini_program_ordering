package com.privatekitchen.admin.service;

import com.privatekitchen.admin.vo.UploadFileVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    UploadFileVO uploadDishImage(MultipartFile file);
}
