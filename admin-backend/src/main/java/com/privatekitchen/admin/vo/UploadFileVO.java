package com.privatekitchen.admin.vo;

import lombok.Data;

@Data
public class UploadFileVO {

    private String fileName;

    private String originalFileName;

    private String url;

    private Long size;
}
