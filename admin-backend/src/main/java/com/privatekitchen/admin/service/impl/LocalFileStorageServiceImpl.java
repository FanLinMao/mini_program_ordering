package com.privatekitchen.admin.service.impl;

import com.privatekitchen.admin.config.UploadProperties;
import com.privatekitchen.admin.service.FileStorageService;
import com.privatekitchen.admin.vo.UploadFileVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private static final DateTimeFormatter FILE_NAME_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final UploadProperties uploadProperties;

    public LocalFileStorageServiceImpl(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public UploadFileVO uploadDishImage(MultipartFile file) {
        validateFile(file);

        String extension = getFileExtension(file.getOriginalFilename());
        String fileName = FILE_NAME_TIME.format(LocalDateTime.now())
                + "_"
                + UUID.randomUUID().toString().replace("-", "")
                + "."
                + extension;

        Path directory = Paths.get(uploadProperties.getDishImageDir()).normalize();
        Path target = directory.resolve(fileName).normalize();

        try {
            Files.createDirectories(directory);
            file.transferTo(target.toFile());
        } catch (IOException error) {
            throw new IllegalStateException("图片上传失败，请检查上传目录是否可写", error);
        }

        UploadFileVO result = new UploadFileVO();
        result.setFileName(fileName);
        result.setOriginalFileName(file.getOriginalFilename());
        result.setUrl(buildFileUrl(fileName));
        result.setSize(file.getSize());
        return result;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("请选择要上传的图片");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        List<String> allowedExtensions = uploadProperties.getAllowedExtensions();
        if (allowedExtensions == null || allowedExtensions.isEmpty()) {
            throw new IllegalStateException("未配置可上传的图片类型，请检查 app.upload.allowed-extensions");
        }

        boolean matched = allowedExtensions.stream()
                .filter(StringUtils::hasText)
                .map(item -> item.trim().toLowerCase(Locale.ROOT))
                .anyMatch(extension::equals);
        if (!matched) {
            throw new IllegalArgumentException("仅支持 " + String.join("、", allowedExtensions) + " 格式图片");
        }
    }

    private String getFileExtension(String fileName) {
        String extension = StringUtils.getFilenameExtension(fileName);
        if (!StringUtils.hasText(extension)) {
            throw new IllegalArgumentException("文件缺少后缀名，无法识别图片类型");
        }
        return extension.trim().toLowerCase(Locale.ROOT);
    }

    private String buildFileUrl(String fileName) {
        String prefix = normalizePath(uploadProperties.getDishImageUrlPrefix());
        if (!StringUtils.hasText(prefix)) {
            return fileName;
        }

        String relativeUrl = prefix + "/" + fileName;
        String baseUrl = normalizeBaseUrl(uploadProperties.getPublicBaseUrl());
        if (!StringUtils.hasText(baseUrl)) {
            return relativeUrl;
        }
        return baseUrl + relativeUrl;
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (!StringUtils.hasText(baseUrl)) {
            return "";
        }
        String normalized = baseUrl.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        String normalized = path.trim().replace("\\", "/");
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
