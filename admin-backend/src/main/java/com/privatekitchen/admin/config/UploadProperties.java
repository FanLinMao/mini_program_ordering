package com.privatekitchen.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.upload")
public class UploadProperties {

    private String dishImageDir = "D:/nginx/static/dishes";

    private String dishImageUrlPrefix = "/static/dishes";

    private String publicBaseUrl = "";

    private List<String> allowedExtensions = new ArrayList<>(List.of("jpg", "jpeg", "png", "gif", "webp"));
}
